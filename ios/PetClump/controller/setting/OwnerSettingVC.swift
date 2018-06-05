//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase

class OwnerSettingVC: GeneralVC{

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
    public static let matchValues = [5, 20, 100, 21000]
    private var lastMatchIndex: Int? = nil


    // Genreated UI
    
    var profile:      OwnerProfile?
    var datePicker:   UIDatePicker?
    var genderPicker: UIPickerView?
    var genderPickerDelegate: GenderInputDelegate?
    var nameInputDelegate: LimitTextFieldDelegate?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        hideKeyboardWhenTappedAround()
        
        // Set up delegate to limit user input to 20 characters
        nameInputDelegate = LimitTextFieldDelegate(count: 20)
        nameTextField.delegate = nameInputDelegate
        
        // Set up datepicker responder
        datePicker = UIDatePicker()
        datePicker!.datePickerMode = .date
        birthdayTextField.inputView = datePicker
        
        // Set up genderpicker responder
        genderPicker = UIPickerView()
        genderPickerDelegate     = GenderInputDelegate(textField: genderTextField)
        genderPicker!.frame      = CGRect(0,0,self.view.bounds.width, 280.0)
        genderPicker!.delegate   = genderPickerDelegate
        genderPicker!.dataSource = genderPickerDelegate
        genderTextField.delegate = genderPickerDelegate
        genderTextField.inputView = genderPicker
        
        // Set up match slider
        matchSlider.minimumValue = 0
        matchSlider.maximumValue = Float(OwnerSettingVC.matchValues.count - 1)
        
        // Assigned by UserDataView
        self.profile = OwnerProfile(id: uid) { profile in
            self.didCompleteDownload(profile: profile)
        }
    }
    
    func didCompleteDownload(profile: OwnerProfile) {
        // Gets user information
        if profile.name == "No name" {
            self.nameTextField.placeholder = "Olivia Hye"
        } else {
            self.nameTextField.text = profile.name
        }
        
        self.genderTextField.text = profile.gender
        
        // Gets user birthdat and parse it
        self.birthdayTextField.text = profile.getBirthdayString()
        self.datePicker!.date = profile.birthday
        
        // Gets weekly schedule
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
    
        // Gets match perference and updates the slider
        let indexAsValue = Float(OwnerSettingVC.matchValues.index(of: profile.distancePerference) ?? 0)
        self.matchSlider.setValue(indexAsValue, animated: true)
        self.updateMatchRangeLabel()
    }
    
    @objc private func toggleImageColor(tapGestureRecognizer: UITapGestureRecognizer){
        let imageView = tapGestureRecognizer.view as! UIImageView
        imageView.backgroundColor =
                imageView.backgroundColor == UIImageView.getDefaultSelectedColor() ?
                UIImageView.getDefaultDeselectedColor() :
                UIImageView.getDefaultSelectedColor()
    }
    
    @IBAction func tapCancel(_ sender: Any) {
        confirmBeforeDismiss(title: NSLocalizedString("Exit", comment: "Alert title when leaving setting page"), message: NSLocalizedString("Your information will not be saved, leave now?", comment: "Alert message when leaving the setting page"))
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
        let range = self.matchSlider.value
        let newIndex = Int(range + 0.5)
        self.matchSlider.setValue(Float(newIndex), animated: false)
        let didChange = lastMatchIndex == nil || newIndex != lastMatchIndex!
        if didChange {
            lastMatchIndex = newIndex
            let actualValue = OwnerSettingVC.matchValues[newIndex]
            self.titleMatchRangeLabel.text = OwnerSettingVC.getRangeDisplayText(actualValue: actualValue)
        }
    }
    
    static func getRangeDisplayText(actualValue: Int)->String{
        return actualValue > 100 ? NSLocalizedString("No prefered range", comment: "Shows on the match range perference slider for maxium range") : NSLocalizedString("With in \(actualValue) km", comment: "Shows on the matching range perference lable")
    }
    
    @IBAction func tapUploadProfile(_ sender: Any) {
        guard (Auth.auth().currentUser != nil) else { return }
        
        // Creates a profile object
        let profile = OwnerProfile()
        if(nameTextField.text! == ""){
            makeAlert(message: NSLocalizedString("You must enter a name!", comment: "Shows when user try to tap save without filling the information"))
            return
        }
        profile.name     = nameTextField.text!
        profile.gender   = genderTextField.text!
        profile.birthday = self.datePicker!.date
        print("self.matchValues[lastMatchIndex ?? 0] : \(OwnerSettingVC.matchValues[lastMatchIndex ?? 0])")
        profile.distancePerference = OwnerSettingVC.matchValues[lastMatchIndex ?? 0]
        
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
        
        // Uploads the profile with empty completed action
        profile.upload(vc: self) {
            OwnerProfile.most_recent_owner = profile
            self.dismiss(animated: true, completion: nil)
        }
    }
}


