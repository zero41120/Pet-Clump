//
//  Profile.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CoreLocation
import Firebase

protocol Profile {
    func generateDictionary() -> [String: Any]
    func upload(vc: QuickAlert)
}


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
    
    let id: String
    var name: String = "No naoe"
    var birthday: Date = Date()
    var gender: String = "Apache"
    var distancePerference: Int = 5
    var lat: Double = 0.0
    var lon: Double = 0.0
    var freeTime = FreeSchedule(freeString: "")
    
    /**
     * This is the id initializer. When loading a profile, only logged in user
     * may construct this object.
     */
    init(id: String) {
        if let _ = Auth.auth().currentUser?.uid{
            self.id = id
        } else {
            self.id = "error_id"
        }
    }
    
    convenience init(dic: [String: Any]){
        print("Constructing with: " + dic.description)
        self.init(id: dic["id"] as! String)
        self.name = dic["name"] as? String ?? self.name
        self.lat = dic["lat"] as? Double  ?? self.lat
        self.lon = dic["lon"] as? Double ?? self.lon
        self.gender = dic["gender"] as? String ?? self.gender
        self.distancePerference = dic["distancePerference"] as? Int ?? self.distancePerference
        self.freeTime = FreeSchedule(freeString: dic["freeTime"] as? String ?? "")
    }
    
    func generateDictionary() -> [String : Any] {
        return [
            "id":   id ,
            "lat":  lat ,
            "lon":  lon ,
            "name": name ,
            "gender":   gender ,
            "birthday": birthday ,
            "freeTime": freeTime.freeString,
            "distancePerference": distancePerference
        ]
    }
    
    func upload(vc: QuickAlert) {
        guard Auth.auth().currentUser != nil else {
            vc.makeAlert(message: "User is not signed in!")
            return
        }
        
        let docRef = Firestore.firestore().collection("users").document(id)
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            if let err = err{
                vc.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
            }
            print("Uploaded successfully for user " + self.id)
        }
    }

}

class FreeSchedule{
    var freeMatrix = Array(repeating: Array(repeating: false, count: 3), count:7)
    var freeString: String
    
    enum PartDay: Int { case Morning = 0, AfterNoon = 1, Night = 2 }
    enum WeekDay: Int {
        case Monday = 0, Tuesday = 1, Wednesday = 2, Thursday = 3, Friday = 4,
             Saturday = 5, Sunday = 6
    }
    
    
    func isFree(weekDay: WeekDay, partDay: PartDay) -> Bool {
        return freeMatrix[weekDay.rawValue][partDay.rawValue]
    }
    
    init(freeString: String){
        self.freeString = freeString
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
}

