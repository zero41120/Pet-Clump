//
//  FirestoreLearningVC.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//


import UIKit
import Firebase

class FirestoreLearningVC: UIViewController{
    
    @IBOutlet weak var topInput: UITextField!
    @IBOutlet weak var butInput: UITextField!
    @IBOutlet weak var uploadButton: UIButton!
    @IBOutlet weak var fetchButton: UIButton!
    
    let debugMode = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }

    @IBAction func tapUpload(_ sender: Any) {
    }
    
    @IBAction func tapFetch(_ sender: Any) {
    }
}
