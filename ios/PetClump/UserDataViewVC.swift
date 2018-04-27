//
//  UserDataSetting.swift
//  PetClump
//
//  Created by admin on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
import UIKit
import Firebase


class UserDataViewVC: UIViewController, QuickAlert{
    
    // Title Labels
    @IBOutlet weak var titleNameLabel:       UILabel!
    @IBOutlet weak var titleMyPetLabel:      UILabel!
    @IBOutlet weak var titleGenderLabel:     UILabel!
    @IBOutlet weak var titleBirthdayLabel:   UILabel!
    @IBOutlet weak var titleMatchRangeLabel: UILabel!
    
    // Information display
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var matchSlider: UISlider!
    @IBOutlet weak var birthdayLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.setupUI()
        self.setupData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.setupData()
    }
    
    func setupUI(){
        let range = 25
        self.titleNameLabel.text       = NSLocalizedString("Name", comment: "This is the title for specifying the name of the user")
        self.titleMyPetLabel.text      = NSLocalizedString("My Pet", comment: "This is the tile for specifying the section of the pet for the user")
        self.titleGenderLabel.text     = NSLocalizedString("Gender", comment: "This is the tile for specifying the gender of the user. It's not the sex of the user.")
        self.titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
        self.hideKeyboardWhenTappedAround()
    }
    
    // This method downloads the user data from Firestore
    func setupData(){
        
        // Verify user is logged in
        guard let id = Auth.auth().currentUser?.uid else { return }
        print("Downloading for id: " + id)

        // Opens document
        let docRef =  Firestore.firestore().collection("users").document(id)
        docRef.getDocument { (document, error) in
            
            // Unwraps data object
            guard let document = document, document.exists else { return }
            let refObj = document.data()!
            print("Finish Downloading: \(refObj.description)")

            // Gets user information
            self.nameLabel.text = refObj["name"] as? String ?? ""
            self.genderLabel.text = refObj["gender"] as? String ?? ""

            // Gets user birthdat and parse it
            if let bd = refObj["birthday"] as? Timestamp{
                let dateFormatter = DateFormatter()
                dateFormatter.dateFormat = "yyyy/MM/dd"
                self.birthdayLabel.text = dateFormatter.string(from: bd.dateValue())
            }

            // Gets match perference and updates the slider
            self.matchSlider.setValue(Float(refObj["distancePerference"] as? Int ?? 25), animated: true)
            let range = Int(self.matchSlider.value)
            self.titleMatchRangeLabel.text = NSLocalizedString("Match Range: \(range)", comment: "This is the label to show the match range from the user to other users. (range) is a computed value and should not be changed")
        }
    }
    
    // Dismisses the view
    @IBAction func tapCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
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
    
    
}

