//
//  SimpleAlert.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import UIKit

/**
 * This estension makes the UIViewController dismiss keyboard when touch non-keyboard area
 */
extension UIViewController {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}


protocol QuickAlert {
    /**
     * This method make a alter with OK button.
     * - Parameter message: A string mess to show in the alter
     */
    func makeAlert(message: String)
}
