//
//  Cryptographer.swift
//  PetClump
//
//  Created by YSH on 5/13/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation

class Cryptographer{
    private static let instance = Cryptographer()
    private init() { }
    
    static func getInstance() -> Cryptographer{
        return self.instance
    }
    
    func generateSecretKey(){
        
    }
    
    func generateInitializationVector(){
        
    }
    
    func encrypt() {
        
    }
    
    func decrypt() {
        
    }
    
    
    
}
