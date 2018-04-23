//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase

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

class UserDataSettingVC: UIViewController, QuickAlert{
    
    // View UI
    @IBOutlet weak var aboutMeLable: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var birthLabel: UILabel!
    @IBOutlet weak var matchRangeLabel: UILabel!
    @IBOutlet weak var myPetsLabel: UILabel!
    
    // Controller UI
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var genderTextField: UITextField!
    @IBOutlet weak var birthdayTextField: UITextField!
    @IBOutlet weak var matchSlider: UISlider!
    @IBOutlet weak var scheduleBut: UIButton!
    @IBOutlet weak var editPhotoBut: UIButton!
    
    // Genreated UI
    var datePicker: UIDatePicker?
    var genderPicker: UIPickerView!
    
    // Data for picker
    let genderPickerData: [String] = [
        NSLocalizedString("Male",   comment: "For picking the gender male"),
        NSLocalizedString("Female", comment: "For picking the gender female"),
        NSLocalizedString("Apache", comment: "For picking the gender Apache, it's an attack helicotper."),
        NSLocalizedString("Other",  comment: "For picking the gender other than male, female, and Apache")
    ]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupUI()
        self.setupData()
    }
    
    // This method downloads the user data from Firestore
    func setupData(){
        if let id = Auth.auth().currentUser?.uid {
            print("Downloading for id: " + id)
            
            // Opens document
            let docRef =  Firestore.firestore().collection("users").document(id)
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    // Unwraps data object
                    let refObj = document.data()!
                    print("Document data: \(refObj.description)")
                    
                    // Gets user information
                    self.nameTextField.text = refObj["name"] as? String ?? ""
                    self.genderTextField.text = refObj["gender"] as? String ?? ""
                    
                    // Gets user birthdat and parse it
                    if let bd = refObj["birthday"] as? Timestamp{
                        let dateFormatter = DateFormatter()
                        dateFormatter.dateFormat = "yyyy/MM/dd"
                        self.birthdayTextField.text = dateFormatter.string(from: bd.dateValue())
                    }
                    
                    // Gets match perference and updates the slider
                    self.matchSlider.setValue(Float(refObj["distancePerference"] as? Int ?? 25), animated: true)
                    self.updateMatchRangeLabel()
                }
            }
        }
    }
    
    func setupUI(){
        // Hide keyboard when touch
        self.hideKeyboardWhenTappedAround()
        
        // Set up datepicker responder
        datePicker = UIDatePicker()
        datePicker?.datePickerMode = .date
        birthdayTextField.inputView = datePicker
        
        // Set up genderpicker responder
        // TODO
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
    
    
    // Conform SimpleAlert
    func makeAlert(message: String){
        // Make alert
        let alert = UIAlertController(title: "Message", message: message, preferredStyle: UIAlertControllerStyle.alert)
        // add an action (button)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        // show the alert
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func slidedMatchRange(_ sender: Any) {
        updateMatchRangeLabel()
    }
    func updateMatchRangeLabel(){
        let range = Int(self.matchSlider.value)
        self.matchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
    }
    
    @IBAction func tapUploadProfile(_ sender: Any) {
        guard (Auth.auth().currentUser != nil) else { return }
        let uid = Auth.auth().currentUser!.uid
        
        // Creates a profile object
        let profile = OwnerProfile(id: uid)
        profile.name     = nameTextField.text!
        profile.gender   = genderTextField.text!
        profile.birthday = self.datePicker!.date
        profile.distancePerference = Int(matchSlider.value)
        
        // Uploads the profile
        profile.upload(vc: self)
        
        // Notify user
        self.makeAlert(message: "Uploaded: " + profile.generateDictionary().description)
        
    }
}

