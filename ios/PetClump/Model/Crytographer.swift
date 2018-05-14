//
//  Cryptographer.swift
//  PetClump
//
//  Created by YSH on 5/13/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import CryptoSwift


class Cryptographer{
    private static let instance = Cryptographer()
    private init() { }
    
    static func getInstance() -> Cryptographer{
        let data = Data(bytes: [0x01, 0x02, 0x03])
        
        let hash = data.md5()
        print("Crypto : \(hash)")
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
