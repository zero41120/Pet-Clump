//
//  StringExtension.swift
//  PetClump
//
//  Created by YSH on 5/16/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation


extension String{
    
    func urlEscape() -> String{
        return self.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!
    }
}
