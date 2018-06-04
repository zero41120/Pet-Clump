//
//  MatchingProfile.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CoreLocation

class MatchingProfile{
    let thisOwner: OwnerProfile
    let thisPet: PetProfile
    let thatOwner: OwnerProfile
    let thatPet: PetProfile
    var quiz: Double = 0.0
    var location: Double = 0.0
    var matchingPercent: Double = 0.0
    var distance: Int = 0

    init(thatOwner: OwnerProfile, thatPet: PetProfile){
        self.thisOwner = OwnerProfile.most_recent_owner!
        self.thisPet = PetProfile.most_recent_pet!
        self.thatPet = thatPet
        self.thatOwner = thatOwner
        self.quiz = self.calculatePercentQuiz(r1: self.thisPet.quiz, r2: self.thatPet.quiz);
        self.location = self.calculatePercentLocation()
        self.matchingPercent = quiz + location
    }
    
    func calculatePercentQuiz(r1: String, r2: String) -> Double{
        // Calculate quiz with 70% weight
        let ra1 = Array(r1), ra2 = Array(r2)
        let size = ra1.count < ra2.count ? ra1.count : ra2.count
        var sum: Double  = 0
        if size == 0 { return 0 }
        for i in 0..<size{
            sum += ra1[i] == ra2[i] ? 1 : 0
        }
        return (sum / Double(size)) * 0.7
    }
    
    func calculatePercentLocation() -> Double{
        if thisOwner.validLocation() && thatOwner.validLocation() {
            let thisLoc = CLLocation(latitude: thisOwner.lat, longitude: thisOwner.lon)
            let thatLoc = CLLocation(latitude: thatOwner.lat, longitude: thatOwner.lon)
            self.distance = Int(thisLoc.distance(from: thatLoc))
            let distance = thisLoc.distance(from: thatLoc)
            let preference = Double(thisOwner.distancePerference)
            return distance < preference/2 ? 0.3 : 0.15
        }
        return 0.0
    }
    
    func getPhotoUrl() -> String{
        return thatPet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
    }
    
    
    func getProfile() -> PetProfile { return self.thatPet }
    func getName() -> String{ return thatPet.name }
    func getAge() -> String{ return thatPet.age }
    func getBio() -> String{ return thatPet.bio }
    func getMatchPercent() -> String { return String(Int(matchingPercent * 100)) + "%" }
    func getData() -> [String: Any]{ return thatPet.generateDictionary() }
    func getPetId() -> String { return "\(thatPet.ownerId)\(thatPet.sequence)" }
    func getSpe() -> String { return thatPet.specie }
    
    static func matcherSorter(this:MatchingProfile, that:MatchingProfile) -> Bool {
        return this.matchingPercent > that.matchingPercent
    }
    
    func toString() -> String{
        return "\(self.matchingPercent): \(self.thatPet.name)"
    }
}
