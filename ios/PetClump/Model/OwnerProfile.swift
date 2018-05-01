//
//  OwnerProfile.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CoreLocation
import Firebase

/**
 * This is the onwer profile data model
 * - id: this should be the uid from Firebase Auth object
 * - name: this should be user's name to show to public
 * - birthday: this is a Date object. In the initlizer, it's a yyyy/MM/dd string
 * - gender: a string of any gender
 * - distancePerference: this shows the user's perference for matching
 * - lat and lon: for location calculation
 * - freeTime: this is a 7*3 bool matrix for free time. In the initlizer, it's a 21 character string with 1 mark as free.
 */
class OwnerProfile: Profile{
    func upload(vc: QuickAlert, completion: ProfileUploader?) {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        
        let docRef = Firestore.firestore().collection("users").document(uid)
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            if let err = err{
                vc.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
            }
            print("Uploaded successfully for user " + uid)
            guard (completion != nil) else { return }
            completion!.didCompleteUpload()
        }
    }
    
    func download(uid: String, completion: ProfileDownloader?) {
        // Opens document
        let docRef =  Firestore.firestore().collection(COLLECTION_NAME).document(uid)
        docRef.getDocument { (document, error) in
            if let document = document, document.exists {
                // Unwraps data object
                let refObj = document.data()!
                print("Document data: \(refObj.description)")
                
                // Gets user information
                self.name = refObj["name"] as? String ?? ""
                self.gender = refObj["gender"] as? String ?? ""
                
                // Gets user birthdat and parse it
                if let bd = refObj["birthday"] as? Timestamp{
                    self.birthday = bd.dateValue()
                }
                
                // Gets match perference and updates the slider
                self.distancePerference = refObj["distancePerference"] as? Int ?? 25
                
                // Gets Freetime and convert to Free schedule
                self.freeTime = FreeSchedule(freeString: refObj["freeTime"] as? String ?? "")
            }
            guard (completion != nil) else { return }
            completion!.didCompleteDownload()
        }
    }
    
    
    private let COLLECTION_NAME:String = "users"
    var name: String = "No name"
    var birthday: Date = Date()
    var gender: String = "Apache"
    var distancePerference: Int = 5
    var lat: Double = 0.0
    var lon: Double = 0.0
    var freeTime = FreeSchedule(freeString: "")

    
    func generateDictionary() -> [String : Any] {
        return [
            "lat":  lat ,
            "lon":  lon ,
            "name": name ,
            "gender":   gender ,
            "birthday": birthday ,
            "freeTime": freeTime.freeTimeAsString,
            "distancePerference": distancePerference
        ]
    }
    
    func getBirthdayString() -> String{
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy/MM/dd"
        return dateFormatter.string(from: self.birthday)
    }
}


class FreeSchedule{
    var freeMatrix = Array(repeating: Array(repeating: false, count: 3), count:7)
    var freeTimeAsString: String
    
    init(freeString: String){
        self.freeTimeAsString = freeString
        var chars = Array(freeString), manCounter = 0, weekCounter = 0
        if chars.count != 21 {
            chars = Array("000000000000000000000")
        }
        for char in chars {
            // 0 is not free, 1 is free
            self.freeMatrix[weekCounter][manCounter] = char == "1"
            manCounter += 1
            manCounter %= 3
            if manCounter == 0 { weekCounter += 1 }
        }
    }
    
    func isFree(weekDay: Int, partDay: Int) -> Bool {
        return freeMatrix[weekDay][partDay]
    }
    
    func getCommonFreeTime(other: FreeSchedule) -> FreeSchedule{
        var commonStirng = ""
        let thisTime = Array(self.freeTimeAsString)
        let otherTime = Array(other.freeTimeAsString)
        for index in 0...thisTime.count {
            if thisTime[index] == "1" && otherTime[index] == "1"{
                commonStirng += "1"
            } else {
                commonStirng += "0"
            }
        }
        return FreeSchedule(freeString: commonStirng)
    }
}

