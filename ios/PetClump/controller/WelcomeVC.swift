//
//  MatchingVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth
import CoreLocation
import UserNotifications

class WelcomeVC: GeneralVC{
    static let locationManager = CLLocationManager()
    static let center = UNUserNotificationCenter.current()
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        guard let uid = Auth.auth().currentUser?.uid else { return }
        
        // First time user
        OwnerProfile.isFirstTimeUsing(uid: uid) { (isFirstTime) in
            if isFirstTime {
                print("Force enter profile page for first time user")
                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                let pdv = storyboard.instantiateViewController(withIdentifier: "OwnerSettingVC") as! OwnerSettingVC
                self.present(pdv, animated: false, completion: nil)
            }
        }
        
        // Set up pet display
        let addPet = NSLocalizedString("Add a New Pet", comment: "Show in the welcome view when the user has not create a pet")
        var titles: [String] = [addPet, addPet, addPet]
        for view in self.view.subviews {
            if let image = view as? UIImageView {
                image.setRounded()
                let _ = PetProfile.init(uid: uid, sequence: image.tag) { (myPet) in
                    let tap: UITapGestureRecognizer
                    if myPet.name == "" {
                        tap = UITapGestureRecognizer(target: self, action: #selector(self.addNewPet(sender:)))
                    } else {
                        titles[image.tag] = myPet.name
                        image.load(url: myPet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
                        tap = UITapGestureRecognizer(target: self, action: #selector(self.startMatching(sender:)))
                    }
                    image.isUserInteractionEnabled = true
                    image.addGestureRecognizer(tap)
                    if let label = self.view.viewWithTag(image.tag + 3) as? UILabel {
                        print("titles: \(titles[image.tag])")
                        label.text = titles[image.tag]
                    }
                }
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        
        
        WelcomeVC.center.requestAuthorization(options: [.alert, .sound]) { (granted, error) in
            if let err = error { print(err) }
        }

        OwnerProfile.most_recent_owner = OwnerProfile(id: uid, completion: { (owner) in
            WelcomeVC.locationManager.requestWhenInUseAuthorization()
            if(CLLocationManager.authorizationStatus() == .authorizedWhenInUse){
                OwnerProfile.most_recent_owner?.upload(vc: nil, completion: {
                    print("silent update location for returning user")
                })
            }
        })
    }
    
    @objc func startMatching(sender: UITapGestureRecognizer){
        guard let uid = Auth.auth().currentUser?.uid else { return }
        let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
        let _ = PetProfile(uid: uid, sequence: sender.view!.tag, completion: { (selectedPet) in
            PetProfile.most_recent_pet = selectedPet
            let pdv = storyBoard.instantiateViewController(withIdentifier: "MainTabBar") as! MainTabBar
            self.present(pdv, animated: true, completion: nil)
        })
    }
    
    @objc func addNewPet(sender: UITapGestureRecognizer){
        guard let uid = Auth.auth().currentUser?.uid else { return }
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "PetSettingVC") as! PetSettingVC
        pdv.petProfile = PetProfile()
        pdv.petProfile!.ownerId = uid
        pdv.petProfile!.sequence = sender.view!.tag
        self.present(pdv, animated: true, completion: nil)
    }
    
    /**
     * This action signs out any Firebase authenticated user if signed in, then alerts the result.
     */
    @IBAction func tapExit(_ sender: Any) {
        let title = NSLocalizedString("Logout", comment: "This is the title in an logout alert")
        let message = NSLocalizedString("Are you sure you want to logout?", comment: "This is the message in an logout alert")
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Logout", comment: "This is the logout button on an alert to inform user"), style: .destructive, handler: { (action: UIAlertAction!) in
            if let user = Auth.auth().currentUser{
                do {
                    print("You are logging out as \(user.uid)")
                    try Auth.auth().signOut()
                } catch let signOutError as NSError {
                    print("Error logging out: \(signOutError)")
                }
            }
            self.dismiss(animated: true, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "This is the Cancel button on an alert to inform user that by clickign this button information on this page stays and user may contiune editing"), style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
}
