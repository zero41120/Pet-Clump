//
//  GenderPicker.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class GenderInputDelegate: NSObject, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate{
    
    // Data for picker
    let genderPickerData: [String] = [
        "-",
        NSLocalizedString("Male",   comment: "For picking the gender male"),
        NSLocalizedString("Female", comment: "For picking the gender female"),
        NSLocalizedString("Apache Helicotper", comment: "For picking the gender Apache, it's an attack helicotper."),
        NSLocalizedString("Other",  comment: "For picking the gender other than male, female, and Apache")
    ]
    
    /**
     * Constructor that gets the UI to update
     */
    private var textField: UITextField!
    init(textField: UITextField){
        self.textField = textField
    }
    
    // Number of rows
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return genderPickerData.count
    }
    
    // Number of column
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // Text on the row
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return genderPickerData[row]
    }
    
    // Selecting completetion
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        textField.text = genderPickerData[row] != "-" ? genderPickerData[row] : genderPickerData.last
    }
    
    // Disable user keyboard entry for text field
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return false
    }
}
