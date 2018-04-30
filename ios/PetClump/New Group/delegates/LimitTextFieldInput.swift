//
//  LimitTextFieldInput.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class LimitTextFieldInput: NSObject, UITextFieldDelegate {
    private let limitCount: Int
    init(count: Int){
        self.limitCount = count
    }
    
    // Disable user to entry more than 20 character
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        print("active delegate")
        guard let text = textField.text else { return true }
        let newLength = text.count + string.count - range.length
        print("new len \(newLength)")
        return newLength <= self.limitCount // Bool
    }
}

