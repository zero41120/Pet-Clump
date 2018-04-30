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

    var profile: PetProfile?
    override func viewDidLoad() {
        super.viewDidLoad()
        print("Id is \(profile!.id)")
        setupUI()
    }
    
    func setupUI(){
        
    }
    
    func onComplete() {
        
    }
    
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
