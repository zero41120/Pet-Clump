//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase

class UserDataSettingVC: UIViewController, ProfileUIUpdater{

    // Profile from UserDataViewVC
    var profile: OwnerProfile = OwnerProfile()
    
    // View UI
    @IBOutlet weak var aboutMeNavBar: UINavigationBar!
    @IBOutlet weak var titleNameLabel: UILabel!
    @IBOutlet weak var titleGenderLabel: UILabel!
    @IBOutlet weak var titleBirthdayLabel: UILabel!
    @IBOutlet weak var titleMatchRangeLabel: UILabel!
    
    // Controller UI
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var genderTextField: UITextField!
    @IBOutlet weak var birthdayTextField: UITextField!
    @IBOutlet weak var matchSlider: UISlider!

    // Genreated UI
    var datePicker: UIDatePicker?
    var genderPicker: UIPickerView?
    var genderPickerDelegate: GenderInput?
    var nameInputDelegate: NameInput?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupUI()
        if let uid = Auth.auth().currentUser?.uid {
            profile.download(id: uid, callerView: self)
        }
    }
    
    
    // This method downloads the user data from Firestore
    func updateUI() {
        // Gets user information
        self.nameTextField.text = profile.name
        self.genderTextField.text = profile.gender
        
        // Gets user birthdat and parse it
        self.birthdayTextField.text = profile.getBirthdayString()
        self.datePicker!.date = profile.birthday
    
        // Gets match perference and updates the slider
        self.matchSlider.setValue(Float(profile.distancePerference), animated: true)
        self.updateMatchRangeLabel()
    }
    
    @objc private func toggleImageColor(tapGestureRecognizer: UITapGestureRecognizer){
        
        let imageView = tapGestureRecognizer.view as! UIImageView
        if imageView.backgroundColor == UIImageView.getDefaultSelectedColor() {
            imageView.backgroundColor = UIImageView.getDefaultDeselectedColor()
            print("\(imageView.tag) is deselected!")
        } else {
            imageView.backgroundColor = UIImageView.getDefaultSelectedColor();
            print("\(imageView.tag) is selected!")
        }
    }
    
    func setupUI(){
        // Hide keyboard when touch
        self.hideKeyboardWhenTappedAround()
        
        // Set up delegate to limit user input to 20 characters
        self.nameInputDelegate = NameInput()
        self.nameTextField.delegate = self.nameInputDelegate

        // Set up datepicker responder
        datePicker = UIDatePicker()
        datePicker!.datePickerMode = .date
        birthdayTextField.inputView = datePicker
        
        // Set up weekly buttons
        let color = UIColor(red:222/255, green:225/255, blue:227/255, alpha: 1).cgColor
        for view in self.view.subviews as [UIView] {
            if let imageView = view as? UIImageView {
                let week = imageView.tag / 10 - 1
                let part = imageView.tag % 10 - 1
                imageView.layer.borderColor = color
                imageView.layer.borderWidth = 1
                imageView.backgroundColor = profile.freeTime.isFree(weekDay: week, partDay: part) ?
                    UIImageView.getDefaultSelectedColor() :
                    UIImageView.getDefaultDeselectedColor()
                
                let tap = UITapGestureRecognizer(target: self, action: #selector(toggleImageColor(tapGestureRecognizer:)))
                imageView.isUserInteractionEnabled = true
                imageView.addGestureRecognizer(tap)
            }
        }
        
        // Set up genderpicker responder
        genderPicker = UIPickerView()
        genderPickerDelegate = GenderInput(textField: genderTextField)
        genderPicker!.delegate = genderPickerDelegate
        genderPicker!.dataSource = genderPickerDelegate
        genderPicker!.frame = CGRect(0,0,self.view.bounds.width, 280.0)
        genderTextField.delegate = genderPickerDelegate
        genderTextField.inputView = genderPicker
    }
    @IBAction func topCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func tapEditBirthday(_sender: Any) {
        // Save date
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .none
        if (self.datePicker != nil) {
            let d = self.datePicker!.date
            birthdayTextField.text = dateFormatter.string(from: d)
        }
        
        self.view.endEditing(true);
    }
    
    @IBAction func slidedMatchRange(_ sender: Any) {
        updateMatchRangeLabel()
    }
    func updateMatchRangeLabel(){
        let range = Int(self.matchSlider.value)
        self.titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
    }
    

    @IBAction func tapUploadProfile(_ sender: Any) {
        guard (Auth.auth().currentUser != nil) else { return }
        let uid = Auth.auth().currentUser!.uid
        
        // Creates a profile object
        let profile = OwnerProfile()
        profile.id       = uid
        profile.name     = nameTextField.text!
        profile.gender   = genderTextField.text!
        profile.birthday = self.datePicker!.date
        profile.distancePerference = Int(matchSlider.value)
        
        //set up the weekly-schedule and svae it.
        var someArray: [String] = [String](repeating: "0", count: 21)
        
        for view in self.view.subviews as [UIView] {
            if let imageView = view as? UIImageView {
        
                let week = imageView.tag / 10
                let part = imageView.tag % 10
                let num = ( week - 1 ) * 3 + part-1
                
                if imageView.backgroundColor == UIImageView.getDefaultSelectedColor() {
                    someArray[num] = "1"
                }
                else {
                    someArray[num] = "0"
                }
            }
        }
        let freeString = someArray.joined(separator: "")
        profile.freeTime = FreeSchedule(freeString: freeString)
        
        
        // Uploads the profile
        profile.upload(vc: self)
        // Exit edit view
        self.dismiss(animated: true, completion: nil)
        
    }
}

class GenderInput: NSObject, UIPickerViewDelegate, UIPickerViewDataSource, UITextFieldDelegate{
    
    
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

class NameInput: NSObject, UITextFieldDelegate {
    // Disable user to entry more than 20 character
    func textField(_ textField: UITextField, shouldChangeCharactersIn range: NSRange, replacementString string: String) -> Bool {
        print("active delegate")
        guard let text = textField.text else { return true }
        let newLength = text.count + string.count - range.length
        print("new len \(newLength)")
        return newLength <= 20 // Bool
    }
}


