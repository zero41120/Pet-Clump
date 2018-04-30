//
//  PetProfile.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CoreLocation
import Firebase

class PetProfile: Profile{
    private let COLLECTION_NAME = "pets"
    var id: String      = "error_id"
    var age: String     = "As old as your grandma"
    var name: String    = "No name"
    var specie: String  = "Pet"
    var ownerId: String = "error_owner_id"
    var mainPhoto: [UInt8] = []
    var photo1: [UInt8] = []
    var photo2: [UInt8] = []
    var photo3: [UInt8] = []
    var photo4: [UInt8] = []
    var photo5: [UInt8] = []
    
    var groupPhoto0: [UInt8] = []
    var groupPhoto1: [UInt8] = []
    var groupPhoto2: [UInt8] = []
    
    func download(id: String, callerView: ProfileUpdater){
        // Opens document
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(id)
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                // Unwraps data object
                let refObj = document.data()!
                print("Document data: \(refObj.description)")
                
                // Gets user information
                self.id      = id
                self.name    = refObj["name"] as? String ?? ""
                self.age     = refObj["age"]  as? String ?? ""
                self.specie  = refObj["spe"]  as? String ?? ""
                self.ownerId = refObj["owner_id"] as? String ?? ""
            }
            callerView.onComplete()
        }
    }
    
    func generateDictionary() -> [String : Any] {
        return [
            "id":   id ,
            "age":  age,
            "name": name,
            "spe":  specie,
            "owner_id": ownerId
        ]
    }
    
    func upload(vc: QuickAlert) {
        guard Auth.auth().currentUser != nil else {
            vc.makeAlert(message: "User is not signed in!")
            return
        }
        
        let docRef = Firestore.firestore().collection(COLLECTION_NAME).document(self.id)
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            if let err = err{
                vc.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
            }
            print("Uploaded successfully for pet " + self.id)
        }
    }
}
