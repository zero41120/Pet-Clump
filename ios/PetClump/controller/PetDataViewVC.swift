//
//  PetDataViewVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class PetDataViewVC: UIViewController, ProfilerDownloader{
    //Title Labels
    @IBOutlet weak var nameTitleLabel: UILabel!
    @IBOutlet weak var petAndOwnerTitleLabel: UILabel!
    @IBOutlet weak var infoTitleLabel: UILabel!
    @IBOutlet weak var petNameTitleLabel: UILabel!
    @IBOutlet weak var petSpeciesLabel: UILabel!
    @IBOutlet weak var petAgeTitleLabel: UILabel!
    @IBOutlet weak var petBioTitleLabel: UILabel!
    //Information display
    @IBOutlet weak var petNameTextField: UITextField!
    @IBOutlet weak var petSpeciesTextField: UITextField!
    @IBOutlet weak var petAgeTextField: UITextField!
    @IBOutlet weak var petBioTextView: UITextView!
    //Pet pictures display
    @IBOutlet weak var bigPetPicture: UIImageView!
    @IBOutlet weak var smallPetPicture1: UIImageView!
    @IBOutlet weak var smallPetPicture2: UIImageView!
    @IBOutlet weak var smallPetPicture3: UIImageView!
    @IBOutlet weak var smallPetPicture4: UIImageView!
    @IBOutlet weak var smallPetPicture5: UIImageView!
    //Pet and Owner Pictures display
    @IBOutlet weak var petAndOwnerPic1: UIImageView!
    @IBOutlet weak var petAndOwnerPic2: UIImageView!
    @IBOutlet weak var petAndOwnerPic3: UIImageView!
    @IBOutlet weak var bioRemainingLabel: UILabel!
    
    var petProfile: PetProfile?
    var speciePicker: UIPickerView?
    var speciePickerDelegate: SpeciePicker?
    var remainingBioDelegate: UITextViewDelegate?
    var nameInputDelegate:  UITextFieldDelegate?
    var ageInputDelegate: UITextFieldDelegate?
    override func viewDidLoad() {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        setupUI()
        petProfile!.download(uid: uid, callerView: self)
    }

    func didCompleteDownload() {
        self.petNameTextField.text = petProfile!.name
        self.petAgeTextField.text  = petProfile!.age
        self.petSpeciesTextField.text  = petProfile!.specie
        self.petBioTextView.text  = petProfile!.bio
        self.bioRemainingLabel.text = "\(petProfile!.bio.count)/500"
    }
    
    func setupUI(){
        self.nameTitleLabel.text        = NSLocalizedString("Pet Name", comment: "This is the title for specifying the name of the pet")
        self.petAndOwnerTitleLabel.text = NSLocalizedString("Pet And I", comment: "This is the title for the picture section of the pet and owner")
        self.infoTitleLabel.text        = NSLocalizedString("INFO", comment: "This is the title for the section of the pet information")
        self.petNameTitleLabel.text     = NSLocalizedString("Name", comment: "This is the title for specifying the name of the pet in the pet info section")
        self.petSpeciesLabel.text       = NSLocalizedString("Species", comment: "This is the title for specifying the species of the pet")
        self.petAgeTitleLabel.text      = NSLocalizedString("Age", comment: "This is the title for specifying the age of the pet")
        self.petBioTitleLabel.text      = NSLocalizedString("Bio", comment: "This is the title for specifying the Bio of the pet")
        
        // Set up specie picker
        speciePicker = UIPickerView()
        speciePickerDelegate = SpeciePicker(textField: petSpeciesTextField)
        speciePicker!.delegate = speciePickerDelegate
        speciePicker!.dataSource = speciePickerDelegate
        petSpeciesTextField.delegate = speciePickerDelegate
        petSpeciesTextField.inputView = speciePicker
        
        // Set up textfield delegates
        remainingBioDelegate = LimitTextViewInput(count: 500, remainingLable: bioRemainingLabel)
        nameInputDelegate = LimitTextFieldInput(count: 20)
        ageInputDelegate = LimitTextFieldInput(count: 50)
        petBioTextView.delegate = remainingBioDelegate
        petNameTextField.delegate = nameInputDelegate
        petAgeTextField.delegate = ageInputDelegate
        
    }
    
    
    @IBAction func tapSave(_ sender: Any) {
        if let uid = Auth.auth().currentUser?.uid{
            petProfile!.name = petNameTextField.text!
            petProfile!.bio  = petBioTextView.text!
            petProfile!.age  = petAgeTextField.text!
            petProfile!.specie = petSpeciesTextField.text!
            petProfile!.ownerId = uid
            petProfile!.upload(vc: self, callerView: nil)
            self.makeAlert(message: NSLocalizedString("Your pet information is saved!", comment: "This is a alter message that shows up and the user tap save on the pet information viewing page."))
        }
        
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
