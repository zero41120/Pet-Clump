//
//  PetDataViewVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class PetDataViewVC: UIViewController, ProfileUpdater{

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
    @IBOutlet weak var petBioTextField: UITextView!
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

    var ownerProfile: OwnerProfile?
    var petProfile:   PetProfile?
    override func viewDidLoad() {
        super.viewDidLoad()
        setupUI()
    }
    
    func setupUI(){
        self.nameTitleLabel.text        = NSLocalizedString("Pet Name", comment: "This is the title for specifying the name of the pet")
        self.petAndOwnerTitleLabel.text = NSLocalizedString("Pet And I", comment: "This is the title for the picture section of the pet and owner")
        self.infoTitleLabel.text        = NSLocalizedString("INFO", comment: "This is the title for the section of the pet information")
        self.petNameTitleLabel.text     = NSLocalizedString("Name", comment: "This is the title for specifying the name of the pet in the pet info section")
        self.petSpeciesLabel.text       = NSLocalizedString("Species", comment: "This is the title for specifying the species of the pet")
        self.petAgeTitleLabel.text      = NSLocalizedString("Age", comment: "This is the title for specifying the age of the pet")
        self.petBioTitleLabel.text      = NSLocalizedString("Bio", comment: "This is the title for specifying the Bio of the pet")
    }
    
    func onComplete() {
        petNameTextField.text = petProfile!.name
        petSpeciesTextField.text = petProfile!.specie
        petAgeTextField.text = petProfile!.age
        petBioTextField.text = petProfile!.bio
        
        
    }
    
    @IBAction func tapSave(_ sender: Any) {
        petProfile!.name = petNameTextField.text!
        petProfile!.bio  = petBioTextField.text!
        petProfile!.age  = petAgeTextField.text!
        petProfile!.specie = petSpeciesTextField.text!
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
