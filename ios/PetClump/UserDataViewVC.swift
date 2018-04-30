//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase


class UserDataViewVC: UIViewController, ProfileUpdater{
    
    var profile: OwnerProfile = OwnerProfile()
    // Title Labels
    @IBOutlet weak var titleNameLabel:       UILabel!
    @IBOutlet weak var titleMyPetLabel:      UILabel!
    @IBOutlet weak var titleGenderLabel:     UILabel!
    @IBOutlet weak var titleBirthdayLabel:   UILabel!
    @IBOutlet weak var titleMatchRangeLabel: UILabel!
    @IBOutlet weak var pet0ImageView: UIImageView!
    @IBOutlet weak var pet1ImageView: UIImageView!
    @IBOutlet weak var pet2ImageView: UIImageView!
    
    // Information display
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var matchSlider: UISlider!
    @IBOutlet weak var birthdayLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupUI()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.setupUI()
    }
    
    @objc private func enterPetView(tapGestureRecognizer: UITapGestureRecognizer){
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "PetDataViewVC") as! PetDataViewVC
        let imageView = tapGestureRecognizer.view as! UIImageView
        let id: String
        switch imageView.tag {
        case  0: id = profile.petId0
        case  1: id = profile.petId1
        default: id = profile.petId2
        }
        pdv.profile = PetProfile()
        pdv.profile!.id = id
        self.present(pdv, animated: true, completion: nil)
    }
    
    func setupUI(){
        let range = 25
        self.titleNameLabel.text       = NSLocalizedString("Name", comment: "This is the title for specifying the name of the user")
        self.titleMyPetLabel.text      = NSLocalizedString("My Pet", comment: "This is the tile for specifying the section of the pet for the user")
        self.titleGenderLabel.text     = NSLocalizedString("Gender", comment: "This is the tile for specifying the gender of the user. It's not the sex of the user.")
        self.titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
        for view in self.view.subviews as [UIView] {
            if let imageView = view as? UIImageView {
                imageView.setRounded()
                let tap = UITapGestureRecognizer(target: self, action: #selector(enterPetView(tapGestureRecognizer:)))
                imageView.isUserInteractionEnabled = true
                imageView.addGestureRecognizer(tap)            }
        }
        self.hideKeyboardWhenTappedAround()
        if let uid = Auth.auth().currentUser?.uid{
            profile.download(id: uid, callerView: self)
        }
    }
    
    // This method downloads the user data from Firestore
    func onComplete() {
        
        // Gets user information
        self.nameLabel.text = profile.name
        self.genderLabel.text = profile.gender
        self.birthdayLabel.text = profile.getBirthdayString()
 
        // Gets match perference and updates the slider
        self.matchSlider.setValue(Float(profile.distancePerference), animated: true)
        let range = Int(self.matchSlider.value)
        self.titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
    }
    
    // Dismisses the view
    @IBAction func tapCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}

