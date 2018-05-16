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
    
    private static let COLLECTION_NAME = "users"
    
    var id = ""
    var name = "No name"
    var gender = "Man"
    var birthday = Date()
    var distancePerference = 5
    var lat = 0.0, lon = 0.0
    var freeTime = FreeSchedule(freeString: "")

    init() {}
    convenience init(id: String, completion: @escaping ( (OwnerProfile)-> Void) ){
        self.init()
        self.id = id
        self.download {
            completion(self)
        }
    }
    
    func upload(vc: QuickAlert?, completion: (() -> Void)?) {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        
        let docRef = Firestore.firestore().collection(OwnerProfile.COLLECTION_NAME).document(uid)
        docRef.setData(self.generateDictionary()) { (err) in
            if err != nil, vc != nil{
                vc!.makeAlert(message: "Upload failed, reason:" + err!.localizedDescription)
                return
            }
            guard (completion != nil) else { return }
            completion!()
        }
    }
    
    func download(completion: (() -> Void)?) {
        guard let _ = Auth.auth().currentUser?.uid else { return }
        
        let docRef = Firestore.firestore().collection(OwnerProfile.COLLECTION_NAME).document(self.id)
        docRef.getDocument { (document, error) in
            if error != nil {
                print(error!)
                return
            }
            if let document = document, document.exists {
                // Unwraps data object
                let refObj = document.data()!
                
                // Gets user information
                self.name = refObj["name"] as? String ?? ""
                self.gender = refObj["gender"] as? String ?? ""
                if let bd = refObj["birthday"] as? Timestamp{
                    self.birthday = bd.dateValue()
                }
                self.distancePerference = refObj["distancePerference"] as? Int ?? 25
                self.freeTime = FreeSchedule(freeString: refObj["freeTime"] as? String ?? "")
            }
            guard (completion != nil) else { return }
            completion!()
        }
    }
    
    private func fetchData(refObj: [String : Any]){
//        self.lon     = refObj["lon"]  as? String ?? ""
//        self.lat     = refObj["lat"]  as? String ?? ""
        self.name    = refObj["name"] as? String ?? ""
        self.gender  = refObj["gender"] as? String ?? ""
        self.freeTime = FreeSchedule(freeString: refObj["freeTime"]  as? String ?? "")
        if let bd = refObj["birthday"] as? Timestamp{
            self.birthday = bd.dateValue()
        }
        self.distancePerference = refObj["distancePerference"] as? Int ?? 25
      
    }
    
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

