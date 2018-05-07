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

class PetProfile: Profile, Deletable{

    private let COLLECTION_NAME = "pets"
    var bio: String     = ""
    var age: String     = ""
    var name: String    = ""
    var quiz: String    = ""
    var specie: String  = "Other"
    var ownerId: String = "error_id"
    var sequence: Int   = 0
    
    // Image
    var url_map = [
        "main_profile_url":"",
        "pet_profile_url_1":"",
        "pet_profile_url_2":"",
        "pet_profile_url_3":"",
        "pet_profile_url_4":"",
        "pet_profile_url_5":"",
        "group_profile_url_1":"",
        "group_profile_url_2":"",
        "group_profile_url_3":""
    ]
    
    public init() {}
    public init(refObj: [String : Any]){
        fetchData(refObj: refObj)
    }
    
    private func fetchData(refObj: [String : Any]){
        self.age     = refObj["age"]  as? String ?? ""
        self.bio     = refObj["bio"]  as? String ?? ""
        self.quiz    = refObj["quiz"] as? String ?? ""
        self.name    = refObj["name"] as? String ?? ""
        self.specie  = refObj["spe"]  as? String ?? ""
        self.ownerId = refObj["owner_id"] as? String ?? ""
        self.url_map["main_profile_url"] = refObj["main_profile_url"] as? String ?? ""
        self.url_map["pet_profile_url_1"] = refObj["pet_profile_url_1"] as? String ?? ""
        self.url_map["pet_profile_url_2"] = refObj["pet_profile_url_2"] as? String ?? ""
        self.url_map["pet_profile_url_3"] = refObj["pet_profile_url_3"] as? String ?? ""
        self.url_map["pet_profile_url_4"] = refObj["pet_profile_url_4"] as? String ?? ""
        self.url_map["pet_profile_url_5"] = refObj["pet_profile_url_5"] as? String ?? ""
        self.url_map["group_profile_url_1"] = refObj["group_profile_url_1"] as? String ?? ""
        self.url_map["group_profile_url_2"] = refObj["group_profile_url_2"] as? String ?? ""
        self.url_map["group_profile_url_3"] = refObj["group_profile_url_3"] as? String ?? ""
    }

    func download(uid: String, completion: ProfileDownloader?) {
        // Opens document
        let generatedId = "\(uid)\(sequence)"
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(generatedId)
        docRef.getDocument { (document, error) in
            if let e = error{ print(e) }
            if let document = document, document.exists {
                // Unwraps data object
                let refObj = document.data()!
                print("Document data: \(refObj.description)")
                
                // Gets user information
                self.fetchData(refObj: refObj)
            }
            guard (completion != nil) else { return }
            completion!.didCompleteDownload()
        }
    }
    
    func generateDictionary() -> [String : Any] {
        return [
            "bio":  bio,
            "age":  age,
            "spe":  specie,
            "name": name,
            "quiz": quiz,
            "owner_id": ownerId,
            "sequence": sequence,
            "main_profile_url":url_map["main_profile_url"] ?? "",
            "pet_profile_url_1":url_map["pet_profile_url_1"] ?? "",
            "pet_profile_url_2":url_map["pet_profile_url_2"] ?? "",
            "pet_profile_url_3":url_map["pet_profile_url_3"] ?? "",
            "pet_profile_url_4":url_map["pet_profile_url_4"] ?? "",
            "pet_profile_url_5":url_map["pet_profile_url_5"] ?? "",
            "group_profile_url_1":url_map["group_profile_url_1"] ?? "",
            "group_profile_url_2":url_map["group_profile_url_2"] ?? "",
            "group_profile_url_3":url_map["group_profile_url_3"] ?? ""
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
    
    func delete(){
        guard let uid = Auth.auth().currentUser?.uid else { return }
        let petId = "\(uid)\(sequence)"
        // TODO delete photos
        Firestore.firestore().collection(COLLECTION_NAME).document(petId).delete(completion: { (error) in
            if let err = error {
                print(err)
            } else {
                print("Pet deleted :(\(petId)")
            }
        })
    }
}
