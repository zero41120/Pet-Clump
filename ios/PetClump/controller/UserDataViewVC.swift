//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase

class UserDataViewVC: UIViewController, ProfileDownloader{
    
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
    
    var profile: OwnerProfile = OwnerProfile()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        setupUI()
        profile.download(uid: uid, completion: self)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.fetchPetImage()
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
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "PetDataViewVC") as! PetDataViewVC
        pdv.petProfile = PetProfile()
        pdv.petProfile!.sequence = sender.view!.tag
        self.present(pdv, animated: true, completion: nil)
    }
    
    private func fetchPetImage(){
        if let uid = Auth.auth().currentUser?.uid{
            profile.download(uid: uid, completion: self)
            let image0 = PetProfileImageDownloader.init(uid: uid, sequence: 0, imageView: pet0ImageView)
            let image1 = PetProfileImageDownloader.init(uid: uid, sequence: 1, imageView: pet1ImageView)
            let image2 = PetProfileImageDownloader.init(uid: uid, sequence: 2, imageView: pet2ImageView)
            image0.download(); image1.download(); image2.download()
        }
    }
    
    // This method downloads the user data from Firestore
    func didCompleteDownload() {
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
