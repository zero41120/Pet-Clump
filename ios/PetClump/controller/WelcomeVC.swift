//
//  MatchingVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth


class WelcomeVC: UIViewController{
    
    override func viewWillAppear(_ animated: Bool) {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        OwnerProfile.isFirstTimeUsing(uid: uid) { (isFirstTime) in
            if isFirstTime {
                print("Force enter profile page for first time user")
                let storyboard = UIStoryboard(name: "Main", bundle: nil)
                let pdv = storyboard.instantiateViewController(withIdentifier: "OwnerSettingVC") as! OwnerSettingVC
                self.present(pdv, animated: false, completion: nil)
            }
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }

        OwnerProfile.most_recent_owner = OwnerProfile(id: uid, completion: { (owner) in
            for view in self.view.subviews {
                if let image = view as? UIImageView {
                    image.setRounded()
                    let _ = PetProfile.init(uid: uid, sequence: image.tag) { (myPet) in
                        image.load(url: myPet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
                        let tap = UITapGestureRecognizer(target: self, action: #selector(self.startMatching(sender:)))
                        image.isUserInteractionEnabled = true
                        image.addGestureRecognizer(tap)
                    }
                }
            }
            OwnerProfile.most_recent_owner?.upload(vc: nil, completion: {
                print("silent update location for returning user")
            })
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
