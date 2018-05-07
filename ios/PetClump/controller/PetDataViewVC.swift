//
//  PetDataViewVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class PetDataViewVC: UIViewController, ProfileDownloader{
    //Title Labels
    @IBOutlet weak var nameTitleLabel:          UILabel!
    @IBOutlet weak var petAndOwnerTitleLabel:   UILabel!
    @IBOutlet weak var infoTitleLabel:          UILabel!
    @IBOutlet weak var petNameTitleLabel:       UILabel!
    @IBOutlet weak var petSpeciesLabel:         UILabel!
    @IBOutlet weak var petAgeTitleLabel:        UILabel!
    @IBOutlet weak var petBioTitleLabel:        UILabel!
    @IBOutlet weak var bioRemainingLabel:       UILabel!

    //Information display
    @IBOutlet weak var petNameTextField:    UITextField!
    @IBOutlet weak var petSpeciesTextField: UITextField!
    @IBOutlet weak var petAgeTextField:     UITextField!
    @IBOutlet weak var petBioTextView:      UITextView!
    
    //Pet pictures display
    @IBOutlet weak var bigPetPicture0:   UIImageView!
    @IBOutlet weak var smallPetPicture1: UIImageView!
    @IBOutlet weak var smallPetPicture2: UIImageView!
    @IBOutlet weak var smallPetPicture3: UIImageView!
    @IBOutlet weak var smallPetPicture4: UIImageView!
    @IBOutlet weak var smallPetPicture5: UIImageView!
    
    //Pet and Owner Pictures display
    @IBOutlet weak var petAndOwnerPic6: UIImageView!
    @IBOutlet weak var petAndOwnerPic7: UIImageView!
    @IBOutlet weak var petAndOwnerPic8: UIImageView!

    // Buttons
    @IBOutlet weak var quizButton: UIButton!
    @IBOutlet weak var deleteButton: UIButton!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var exitButton: UIBarButtonItem!
    
    var petProfile:     PetProfile?
    var speciePicker:   UIPickerView?
    var ageInputDelegate:   UITextFieldDelegate?
    var nameInputDelegate:  UITextFieldDelegate?
    var speciePickerDelegate: SpeciePicker?
    var remainingBioDelegate: UITextViewDelegate?
    var imagePickerDelegate: ImagePicker?
    
    @IBAction func tapOnImageView(_ sender: UITapGestureRecognizer) {
        let imageView = sender.view as! UIImageView
        let alert = UIAlertController(title: NSLocalizedString("Image Function", comment: "This is an alert title when user clicks on the image to upload or delete"), message: "", preferredStyle: UIAlertControllerStyle.actionSheet)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Upload a new image", comment: "This is a button to indicate user to upload a new image."), style: .default, handler: { (action: UIAlertAction!) in
            self.imagePickerDelegate = ImagePicker(imageView: imageView, profile: self.petProfile!)
            let imagePicker = UIImagePickerController()
            imagePicker.sourceType = .photoLibrary
            imagePicker.delegate = self.imagePickerDelegate!
            self.present(imagePicker, animated: true, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (action: UIAlertAction!) in
            imageView.image = nil;
            imageView.backgroundColor = UIImageView.getDefaultDeselectedColor()
            switch imageView.tag {
            case 0: self.petProfile!.url_map["main_profile_url"] = ""
            case 1: self.petProfile!.url_map["pet_profile_url_1"] = ""
            case 2: self.petProfile!.url_map["pet_profile_url_2"] = ""
            case 3: self.petProfile!.url_map["pet_profile_url_3"] = ""
            case 4: self.petProfile!.url_map["pet_profile_url_4"] = ""
            case 5: self.petProfile!.url_map["pet_profile_url_5"] = ""
            case 6: self.petProfile!.url_map["group_profile_url_1"] = ""
            case 7: self.petProfile!.url_map["group_profile_url_2"] = ""
            case 8: self.petProfile!.url_map["group_profile_url_3"] = ""
            default: break
            }
            self.petProfile?.upload(vc: nil, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        self.present(alert, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let _ = Auth.auth().currentUser?.uid {
            self.setupUI()
            setupDelegate()
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        petProfile!.download(uid: uid, completion: self)
    }
    
    // Pre-fill text fields when pet info is downloaded from Firebase
    func didCompleteDownload() {
        self.petBioTextView.text        = petProfile!.bio
        self.petAgeTextField.text       = petProfile!.age
        self.petNameTextField.text      = petProfile!.name
        self.bioRemainingLabel.text     = "\(petProfile!.bio.count)/500"
        self.petSpeciesTextField.text   = petProfile!.specie
        self.quizButton.titleLabel!.text = NSLocalizedString("Start Quiz (\(petProfile!.quiz.count)/100)", comment: "This is the button that takes the user to quiz view. It shows how many quiz this user has complete for this particular pet")
        self.deleteButton.addTarget(self, action: #selector(deletePet), for: .touchUpInside)
        self.bigPetPicture0.load(url: self.petProfile!.url_map["main_profile_url"] ?? "")
        self.smallPetPicture1.load(url: self.petProfile!.url_map["pet_profile_url_1"] ?? "")
        self.smallPetPicture2.load(url: self.petProfile!.url_map["pet_profile_url_2"] ?? "")
        self.smallPetPicture3.load(url: self.petProfile!.url_map["pet_profile_url_3"] ?? "")
        self.smallPetPicture4.load(url: self.petProfile!.url_map["pet_profile_url_4"] ?? "")
        self.smallPetPicture5.load(url: self.petProfile!.url_map["pet_profile_url_5"] ?? "")
        self.petAndOwnerPic6.load(url: self.petProfile!.url_map["group_profile_url_1"] ?? "")
        self.petAndOwnerPic7.load(url: self.petProfile!.url_map["group_profile_url_2"] ?? "")
        self.petAndOwnerPic8.load(url: self.petProfile!.url_map["group_profile_url_3"] ?? "")
    }
    @objc private func deletePet(){
        confirmBeforeDelete(title: NSLocalizedString("Delete this pet?", comment: "This is the title on an alert when user clicks delete pet"), message: NSLocalizedString("Are you sure you want to delete this pet? This action cannot be undone.", comment: "This is the message when the user clicks delete pet"), toDelete: petProfile!)
    }
    
    private func setupUI(){
        self.nameTitleLabel.text        = NSLocalizedString("Pet Name", comment: "This is the title for specifying the name of the pet")
        self.infoTitleLabel.text        = NSLocalizedString("INFO", comment: "This is the title for the section of the pet information")
        self.petSpeciesLabel.text       = NSLocalizedString("Species", comment: "This is the title for specifying the species of the pet")
        self.petAgeTitleLabel.text      = NSLocalizedString("Age", comment: "This is the title for specifying the age of the pet")
        self.petBioTitleLabel.text      = NSLocalizedString("Bio", comment: "This is the title for specifying the Bio of the pet")
        self.petNameTitleLabel.text     = NSLocalizedString("Name", comment: "This is the title for specifying the name of the pet in the pet info section")
        self.petAndOwnerTitleLabel.text = NSLocalizedString("Pet And I", comment: "This is the title for the picture section of the pet and owner")
    }
    
    private func setupDelegate(){
        // Specie picker
        speciePicker = UIPickerView()
        speciePickerDelegate = SpeciePicker(textField: petSpeciesTextField)
        speciePicker!.delegate = speciePickerDelegate
        speciePicker!.dataSource = speciePickerDelegate
        petSpeciesTextField.delegate = speciePickerDelegate
        petSpeciesTextField.inputView = speciePicker
        
        // Text fields and textview delegates
        nameInputDelegate = LimitTextFieldInput(count: 20)
        ageInputDelegate  = LimitTextFieldInput(count: 50)
        remainingBioDelegate = LimitTextViewInput(count: 500, remainingLable: bioRemainingLabel)
        petBioTextView.makeTextField(delegate: remainingBioDelegate!)
        petNameTextField.delegate = nameInputDelegate
        petAgeTextField.delegate  = ageInputDelegate
    }
    
    
    @IBAction func tapSave(_ sender: Any) {
        if let uid = Auth.auth().currentUser?.uid{
            petProfile!.name = petNameTextField.text!
            petProfile!.bio  = petBioTextView.text!
            petProfile!.age  = petAgeTextField.text!
            petProfile!.specie = petSpeciesTextField.text!
            petProfile!.ownerId = uid
            petProfile!.upload(vc: self, completion: nil)
            self.makeAlert(message: NSLocalizedString("Your pet information is saved!", comment: "This is a alter message that shows up and the user tap save on the pet information viewing page."))
        }
        
    }
    
    @IBAction func tapQuiz(_ sender: Any){
        if petProfile!.quiz.count == 100 {
            makeAlert(message: NSLocalizedString("You have complete all the questions!", comment: "This is an alert message when the user clicks the Start Quiz button but have finished all 100 questions"))
        }
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let qvc = storyBoard.instantiateViewController(withIdentifier: "QuizVC") as! QuizVC
        qvc.petProfile = PetProfile()
        qvc.petProfile!.sequence = self.petProfile!.sequence
        self.present(qvc, animated: true, completion: nil)
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
