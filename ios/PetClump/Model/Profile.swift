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
    var id: String      { get set }
    var name: String    { get set }
    var birthday: Date  { get set }
    var gender: String  { get set }
    func generateDictionary() -> [String: Any]
    func upload(vc: QuickAlert)
}


class OwnerProfile: Profile{
   
    
    var id: String
    var name: String
    var birthday: Date
    var gender: String
    var distancePerference: Int = 5
    var freeTime: FreeSchedule = FreeSchedule(freeString: "")
    
    init(id: String, name: String, birthday: Date, gender: String) {
        self.id = id
        self.name = name
        self.birthday = birthday
        self.gender = gender
    }
    
    func generateDictionary() -> [String : Any] {
        return [
            "id": self.id,
            "name": self.name,
            "birthday": self.birthday,
            "gender": self.gender,
            "distancePerference": self.distancePerference,
            "freeTime": self.freeTime.freeString
        ]
    }
    func upload(vc: QuickAlert) {
        guard Auth.auth().currentUser != nil else {
            vc.makeAlert(message: "User is not signed in!")
            return
        }
        let docRef = Firestore.firestore().document("users/owner_profile")
        docRef.setData(self.generateDictionary()) { (err: Error?) in
            if let err = err{
                vc.makeAlert(message: "Upload failed, reason:" + err.localizedDescription)
            }
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

