//
//  PetDataSettingVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit


class PetDataSettingVC: UIViewController{
    
    private var profile: PetProfile = PetProfile()
    private var bioLimitDelegate: LimitTextFieldInput?
    private var nameDelegate: LimitTextFieldInput?
    private var ageDelegate: LimitTextFieldInput?
    private var specitDelegate: LimitTextFieldInput?
    
    override func viewDidLoad() {
        super.viewDidLoad()
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
