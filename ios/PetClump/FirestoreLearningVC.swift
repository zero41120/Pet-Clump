//
//  FirestoreLearningVC.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//


import UIKit
import Firebase

class FirestoreLearningVC: UIViewController{
    
    @IBOutlet weak var topInput: UITextField!
    @IBOutlet weak var botInput: UITextField!
    @IBOutlet weak var uploadButton: UIButton!
    @IBOutlet weak var fetchButton: UIButton!
    @IBOutlet weak var displayLabel: UILabel!
    @IBOutlet weak var downloadLabel: UILabel!
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    let debugMode = true
    let db = Firestore.firestore()
    var docRef: DocumentReference!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        docRef = Firestore.firestore().document("users/example")
    }

    @IBAction func tapUpload(_ sender: Any) {
        guard Auth.auth().currentUser != nil else {
            makeAlert(message: "User not signed in!")
            return
        }
        
        let uploadDic: [String: String] = [
            "id": Auth.auth().currentUser!.uid,
            "top": topInput.text ?? "Top no text",
            "bot": botInput.text ?? "Bottom no text"
        ]
        
        docRef.setData(uploadDic) { (err) in
            var message = "Upload status: "
            message += (err != nil) ? "Upload failed!" + err!.localizedDescription: "Success!"
            self.makeAlert(message: message)
        }

    }
    
    @IBAction func tapFetch(_ sender: Any) {
        docRef.getDocument { (snapshot, err) in
            guard let docSnap = snapshot, docSnap.exists else { return }
            let myData = docSnap.data()
            let top = myData!["top"] as? String ?? ""
            let bot = myData!["bot"] as? String ?? ""
            self.displayLabel.text = top + " : " + bot
        }
    }
    
    @IBAction func tapUploadProfile(_ sender: Any) {
        if let id = Auth.auth().currentUser?.uid {
            let profile = OwnerProfile()
            profile.id = id
            profile.name = "Jimmy"
            profile.upload(vc: self)
        }
    }
    @IBAction func tapDownloadProfile(_ sender: Any) {
        if let id = Auth.auth().currentUser?.uid {
            var retObj: [String : Any] = ["id":"Error"]
            print("Downloading for id: " + id)
            let docRef =  Firestore.firestore().collection("users").document(id)
            docRef.getDocument { (document, error) in
                if let document = document, document.exists {
                    retObj = document.data()!
                    let dataDescription = retObj.description
                    print("Document data: \(dataDescription)")
                    self.downloadLabel.text = dataDescription
                } else {
                    print("Document does not exist")
                }
            }
        }
    }
}
