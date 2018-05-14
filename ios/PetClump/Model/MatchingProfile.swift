//
//  MatchingProfile.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation

class MatchingProfile{
    let profile: PetProfile
    var matchingPercent: Double = 0
    let distance: Int = 0

    init(quizResult: String, petProfile: PetProfile){
        profile = petProfile
        matchingPercent = calculatePercent(r1: quizResult, r2: petProfile.quiz);
    }
    
    func calculatePercent(r1: String, r2: String) -> Double{
        let ra1 = Array(r1), ra2 = Array(r2)
        let size = ra1.count < ra2.count ? ra1.count : ra2.count
        var sum: Double  = 0
        if size == 0 { return 0 }
        for i in 0..<size{
            sum += ra1[i] == ra2[i] ? 1 : 0
        }
        return sum / Double(size)
    }
    
    func getPhotoUrl() -> String{
        return profile.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
    }
    
    
    func getProfile() -> PetProfile { return self.profile }
    func getName() -> String{ return profile.name }
    func getAge() -> String{ return profile.age }
    func getBio() -> String{ return profile.bio }
    func getMatchPercent() -> String { return String(Int(matchingPercent * 100)) + "%" }
    func getData() -> [String: Any]{ return profile.generateDictionary() }
    func getPetId() -> String { return "\(profile.ownerId)\(profile.sequence)" }
    func getSpe() -> String { return profile.specie }
    
    static func matcherSorter(this:MatchingProfile, that:MatchingProfile) -> Bool {
        return this.matchingPercent > that.matchingPercent
    }
    
    func toString() -> String{
        return "\(self.matchingPercent): \(self.profile.name)"
    }
}
