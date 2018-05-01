//
//  TextViewExtension.swift
//  PetClump
//
//  Created by YSH on 5/1/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit


extension UITextView {
    
    func makeTextField(delegate: UITextViewDelegate){
        self.delegate = delegate
        self.isEditable = true
        self.layer.backgroundColor = UIColor.white.cgColor
        self.layer.borderColor = UIColor.gray.withAlphaComponent(0.2).cgColor
        self.layer.borderWidth = 1.0
        self.layer.cornerRadius = 8.0
        self.layer.masksToBounds = true
    }
    
}
