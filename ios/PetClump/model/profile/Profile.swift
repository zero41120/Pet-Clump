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
    /**
     * This method generates the dictionary definion on firebase
     */
    func generateDictionary() -> [String: Any]
    /**
     * This method uploads this profile to firebase
     * - parameter vc: a quick alter class for debug purpose.
     */
    func upload(vc: QuickAlert?, completion: ( ()->Void )?)
    /**
     * This method downloads a profile from firebase,
     * you must insert the variable for the key before calling this method
     * - parameter vc: a quick alter class for debug purpose.
     */
    func download(completion: ( ()->Void )?)
}

protocol Deletable {
    func delete()
}

