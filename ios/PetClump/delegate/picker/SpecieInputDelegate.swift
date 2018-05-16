//
//  SpeciePicker.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import UIKit


class SpecieInputDelegate: NSObject, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate{
    
    // Data for picker
    let speciePickerData: [String] = [
        "-",
        NSLocalizedString("DOG",     comment: "For the name of the animal specie"),
        NSLocalizedString("CAT",     comment: "For the name of the animal specie"),
        NSLocalizedString("BIRD",    comment: "For the name of the animal specie"),
        NSLocalizedString("ANT",     comment: "For the name of the animal specie"),
        NSLocalizedString("FERRET",  comment: "For the name of the animal specie"),
        NSLocalizedString("FISH",    comment: "For the name of the animal specie"),
        NSLocalizedString("FROG",    comment: "For the name of the animal specie"),
        NSLocalizedString("GOAT",    comment: "For the name of the animal specie"),
        NSLocalizedString("HAMSTER", comment: "For the name of the animal specie"),
        NSLocalizedString("HORSE",   comment: "For the name of the animal specie"),
        NSLocalizedString("PIG",     comment: "For the name of the animal specie"),
        NSLocalizedString("RABBIT",  comment: "For the name of the animal specie"),
        NSLocalizedString("SNAKE",   comment: "For the name of the animal specie"),
        NSLocalizedString("TURTLE",  comment: "For the name of the animal specie"),
        NSLocalizedString("OCTOPUS", comment: "For the name of the animal specie"),
        NSLocalizedString("LLAMA",   comment: "For the name of the animal specie"),
        NSLocalizedString("EAGLE",   comment: "For the name of the animal specie"),
        NSLocalizedString("LIZARD",  comment: "For the name of the animal specie"),
        NSLocalizedString("OTHER",   comment: "For the name of the animal specie")
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
        return speciePickerData.count
    }
    
    // Number of column
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    // Text on the row
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return speciePickerData[row]
    }
    
    // Selecting completetion
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        textField.text = speciePickerData[row] != "-" ? speciePickerData[row] : speciePickerData.last
    }
    
    // Disable user keyboard entry for text field
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        return false
    }
}
