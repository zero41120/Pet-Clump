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
    var bio: String     = ""
    var age: String     = ""
    var name: String    = ""
    var specie: String  = "Other"
    var ownerId: String = "error_id"
    var sequence: Int   = 0
    // Image
    var mainPhoto: [UInt8] = []
    var photo1: [UInt8] = []
    var photo2: [UInt8] = []
    var photo3: [UInt8] = []
    var photo4: [UInt8] = []
    var photo5: [UInt8] = []
    // Group photo
    var groupPhoto0: [UInt8] = []
    var groupPhoto1: [UInt8] = []
    var groupPhoto2: [UInt8] = []

    func download(uid: String, completion: ProfileDownloader?) {
        // Opens document
        let generatedId = "\(uid)\(sequence)"
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(generatedId)
        docRef.getDocument { (document, error) in
            if let e = error{
                print(e)
            }
            if let document = document, document.exists {
                // Unwraps data object
                let refObj = document.data()!
                print("Document data: \(refObj.description)")
                
                // Gets user information
                self.name    = refObj["name"] as? String ?? ""
                self.age     = refObj["age"]  as? String ?? ""
                self.bio     = refObj["bio"]  as? String ?? ""
                self.specie  = refObj["spe"]  as? String ?? ""
                self.ownerId = refObj["owner_id"] as? String ?? ""
            }
            guard (completion != nil) else { return }
            completion!.didCompleteDownload()
        }
    }
    
    func generateDictionary() -> [String : Any] {
        return [
            "bio":  bio,
            "age":  age,
            "name": name,
            "spe":  specie,
            "owner_id": ownerId,
            "sequence": sequence
        ]
    }
    
    func upload(vc: QuickAlert, completion: ProfileUploader?) {
        guard let uid = Auth.auth().currentUser?.uid else {
            vc.makeAlert(message: "User is not signed in!")
            return
        }
        let generatedId = "\(uid)\(sequence)"
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(generatedId)
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            if let err = err{
                vc.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
            }
            print("Uploaded successfully for pet " + generatedId)
            guard (completion != nil) else { return }
            completion!.didCompleteUpload()
        }
    }
}
