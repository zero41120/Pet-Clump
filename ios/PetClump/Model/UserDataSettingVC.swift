//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase


class UserDataSettingVC: UIViewController{
    
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
    
    ///below is the IBOutlet of each time slot when choosing weekly schedule.
    //monday
    @IBOutlet weak var amMo: UIImageView!
    @IBOutlet weak var noMo: UIImageView!
    @IBOutlet weak var pmMo: UIImageView!
    //tuesday
    @IBOutlet weak var amTu: UIImageView!
    @IBOutlet weak var noTu: UIImageView!
    @IBOutlet weak var pmTu: UIImageView!
    //wednesday
    @IBOutlet weak var amWe: UIImageView!
    @IBOutlet weak var noWe: UIImageView!
    @IBOutlet weak var pmWe: UIImageView!
    //thursday
    @IBOutlet weak var amTh: UIImageView!
    @IBOutlet weak var noTh: UIImageView!
    @IBOutlet weak var pmTh: UIImageView!
    //friday
    @IBOutlet weak var amFr: UIImageView!
    @IBOutlet weak var noFr: UIImageView!
    @IBOutlet weak var pmFr: UIImageView!
    //saturday
    @IBOutlet weak var amSa: UIImageView!
    @IBOutlet weak var noSa: UIImageView!
    @IBOutlet weak var pmSa: UIImageView!
    //sunday
    @IBOutlet weak var amSu: UIImageView!
    @IBOutlet weak var noSu: UIImageView!
    @IBOutlet weak var pmSu: UIImageView!
    
   
    
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
                        self.datePicker!.date = bd.dateValue()
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
        let profile = OwnerProfile(id: uid)
        profile.name     = nameTextField.text!
        profile.gender   = genderTextField.text!
        profile.birthday = self.datePicker!.date
        profile.distancePerference = Int(matchSlider.value)
        
        // Uploads the profile
        profile.upload(vc: self)
        
        // Exit edit view
        self.dismiss(animated: true, completion: nil)
        
    }
}
