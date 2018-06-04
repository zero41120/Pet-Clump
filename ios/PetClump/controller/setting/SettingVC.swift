//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase

class SettingVC: UIViewController{
    
    // Title Labels
    @IBOutlet weak var titleNameLabel:       UILabel!
    @IBOutlet weak var titleMyPetLabel:      UILabel!
    @IBOutlet weak var titleGenderLabel:     UILabel!
    @IBOutlet weak var titleBirthdayLabel:   UILabel!
    @IBOutlet weak var titleMatchRangeLabel: UILabel!
    
    // Image view
    @IBOutlet weak var pet0ImageView: UIImageView!
    @IBOutlet weak var pet1ImageView: UIImageView!
    @IBOutlet weak var pet2ImageView: UIImageView!
    
    // Information display
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var matchSlider: UISlider!
    @IBOutlet weak var birthdayLabel: UILabel!
    
    var profile: OwnerProfile?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        setupUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        profile = OwnerProfile(id: uid) { profile in
            // Gets user information
            self.nameLabel.text = profile.name
            self.genderLabel.text = profile.gender
            self.birthdayLabel.text = profile.getBirthdayString()
            
            // Gets match perference and updates the slider
            self.matchSlider.minimumValue = 0
            self.matchSlider.maximumValue = Float(OwnerSettingVC.matchValues.count - 1)
            let indexAsValue = Float(OwnerSettingVC.matchValues.index(of: profile.distancePerference) ?? 0)
            self.matchSlider.setValue(indexAsValue, animated: true)
            self.titleMatchRangeLabel.text = OwnerSettingVC.getRangeDisplayText(actualValue: profile.distancePerference)
        }
        let _ = PetProfile(uid: uid, sequence: 0) { (pet) in
            self.pet0ImageView.load(url: pet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
        }
        let _ = PetProfile(uid: uid, sequence: 1) { (pet) in
            self.pet1ImageView.load(url: pet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
        }
        let _ = PetProfile(uid: uid, sequence: 2) { (pet) in
            self.pet2ImageView.load(url: pet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
        }
    }
    
    func setupUI(){
        let range = 25
        hideKeyboardWhenTappedAround()
        titleNameLabel.text       = NSLocalizedString("Name", comment: "This is the title for specifying the name of the user")
        titleMyPetLabel.text      = NSLocalizedString("My Pet", comment: "This is the tile for specifying the section of the pet for the user")
        titleGenderLabel.text     = NSLocalizedString("Gender", comment: "This is the tile for specifying the gender of the user. It's not the sex of the user.")
        titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
        for view in self.view.subviews as [UIView] {
            if let imageView = view as? UIImageView {
                imageView.setRounded()
                let tap = UITapGestureRecognizer(target: self, action: #selector(enterPetView(sender:)))
                imageView.isUserInteractionEnabled = true
                imageView.addGestureRecognizer(tap)
            }
        }
    }
    
    @objc private func enterPetView(sender: UITapGestureRecognizer){
        // Present Pet data view
        guard let uid = Auth.auth().currentUser?.uid else { return }
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "PetSettingVC") as! PetSettingVC
        pdv.petProfile = PetProfile()
        pdv.petProfile!.ownerId = uid
        pdv.petProfile!.sequence = sender.view!.tag
        self.present(pdv, animated: true, completion: nil)
    }
    
    // Dismisses the view
    @IBAction func tapCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
