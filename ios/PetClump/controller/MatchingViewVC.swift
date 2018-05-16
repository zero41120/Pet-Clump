//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth

class MatchingViewVC: UIViewController{
    
    // Assigned by caller view
    var petProfile: PetProfile?
    
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var specieLabel: UILabel!
    @IBOutlet weak var bioTextField: UITextView!
    @IBOutlet weak var imageScroller: ImageScrollerView!
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        ageLabel.text = petProfile!.age
        nameLabel.text = petProfile!.name
        specieLabel.text = petProfile!.specie
        bioTextField.text = petProfile!.bio
        let imageUrls = petProfile!.getPhotoUrls(isPulic: true)
        self.imageScroller.setupScrollerWith(urls: imageUrls)
    }
}