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
    @IBOutlet weak var bigPetPicture:    UIImageView!
    @IBOutlet weak var smallPetPicture1: UIImageView!
    @IBOutlet weak var smallPetPicture2: UIImageView!
    @IBOutlet weak var smallPetPicture3: UIImageView!
    @IBOutlet weak var smallPetPicture4: UIImageView!
    @IBOutlet weak var smallPetPicture5: UIImageView!
    //Pet and Owner Pictures display
    @IBOutlet weak var petAndOwnerPic1: UIImageView!
    @IBOutlet weak var petAndOwnerPic2: UIImageView!
    @IBOutlet weak var petAndOwnerPic3: UIImageView!

    @IBOutlet weak var quizButton: UIButton!

    var petProfile:     PetProfile?
    var speciePicker:   UIPickerView?
    var ageInputDelegate:   UITextFieldDelegate?
    var nameInputDelegate:  UITextFieldDelegate?
    var speciePickerDelegate: SpeciePicker?
    var remainingBioDelegate: UITextViewDelegate?
    
    
    override func viewDidLoad() {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        setupUI()
        setupDelegate()
        petProfile!.download(uid: uid, completion: self)
    }

    // Pre-fill text fields when pet info is downloaded from Firebase
    func didCompleteDownload() {
        self.petBioTextView.text        = petProfile!.bio
        self.petAgeTextField.text       = petProfile!.age
        self.petNameTextField.text      = petProfile!.name
        self.bioRemainingLabel.text     = "\(petProfile!.bio.count)/500"
        self.petSpeciesTextField.text   = petProfile!.specie
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
