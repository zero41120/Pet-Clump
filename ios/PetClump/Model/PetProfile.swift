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
    private var url_map = [
        "main_url":"",
        "pet_view_1":"",
        "pet_view_2":"",
        "pet_view_3":"",
        "pet_view_4":"",
        "pet_view_5":"",
        "group_view_1":"",
        "group_view_2":"",
        "group_view_3":""
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
        self.url_map["main_url"] = refObj["main_url"] as? String ?? ""
        self.url_map["pet_view_1"] = refObj["pet_view_1"] as? String ?? ""
        self.url_map["pet_view_2"] = refObj["pet_view_2"] as? String ?? ""
        self.url_map["pet_view_3"] = refObj["pet_view_3"] as? String ?? ""
        self.url_map["pet_view_4"] = refObj["pet_view_4"] as? String ?? ""
        self.url_map["pet_view_5"] = refObj["pet_view_5"] as? String ?? ""
        self.url_map["group_view_1"] = refObj["group_view_1"] as? String ?? ""
        self.url_map["group_view_2"] = refObj["group_view_2"] as? String ?? ""
        self.url_map["group_view_3"] = refObj["group_view_3"] as? String ?? ""
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
                print("Dic: \(self.generateDictionary())")
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
            "main_url": url_map["main_url"] ?? "",
            "pet_view_1": url_map["pet_view_1"] ?? "",
            "pet_view_2": url_map["pet_view_2"] ?? "",
            "pet_view_3": url_map["pet_view_3"] ?? "",
            "pet_view_4": url_map["pet_view_4"] ?? "",
            "pet_view_5": url_map["pet_view_5"] ?? "",
            "group_view_1": url_map["group_view_1"] ?? "",
            "group_view_2": url_map["group_view_2"] ?? "",
            "group_view_3": url_map["group_view_3"] ?? ""
        ]
    }
    
    enum PetPhotoUrlKey {
        case main, pet1, pet2, pet3, pet4, pet5, group1, group2, group3
    }
    
    func getPhotoUrl(key: PetPhotoUrlKey) -> String{
        switch key {
        case .main: return url_map["main_url"] ?? ""
        case .pet1: return url_map["pet_view_1"] ?? ""
        case .pet2: return url_map["pet_view_2"] ?? ""
        case .pet3: return url_map["pet_view_3"] ?? ""
        case .pet4: return url_map["pet_view_4"] ?? ""
        case .pet5: return url_map["pet_view_5"] ?? ""
        case .group1: return url_map["group_view_1"] ?? ""
        case .group2: return url_map["group_view_2"] ?? ""
        case .group3: return url_map["group_view_3"] ?? ""
        }
    }
    
    func setPhotoUrl(key: PetPhotoUrlKey, url: String){
        switch key {
        case .main: url_map["main_url"] = url
        case .pet1: url_map["pet_view_1"] = url
        case .pet2: url_map["pet_view_2"] = url
        case .pet3: url_map["pet_view_3"] = url
        case .pet4: url_map["pet_view_4"] = url
        case .pet5: url_map["pet_view_5"] = url
        case .group1: url_map["group_view_1"] = url
        case .group2: url_map["group_view_2"] = url
        case .group3: url_map["group_view_3"] = url
        }
    }
    
    func upload(vc: QuickAlert?, completion: ProfileUploader?) {
        guard let uid = Auth.auth().currentUser?.uid else {
            guard vc != nil else { return }
            vc!.makeAlert(message: "User is not signed in!")
            return
        }
        let generatedId = "\(uid)\(sequence)"
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(generatedId)
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            print("Before upload: \(self.generateDictionary())")
            if let err = err{
                guard vc != nil else { return }
                vc!.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
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
