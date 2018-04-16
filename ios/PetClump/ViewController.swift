//
//  ViewController.swift
//  PetClump
//
//  Created by YSH on 4/11/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    @IBOutlet weak var LabelEric: UILabel!
    @IBOutlet weak var LabelJerod: UILabel!
    @IBOutlet weak var LabelGalen: UILabel!
    @IBOutlet weak var LabelTamir: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        setupView()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func setupView() {
        self.LabelEric.text =
            NSLocalizedString("Hello, My name is Eric.", comment: "A greeting string for the main view with Eric as the name")
        
    }


}

