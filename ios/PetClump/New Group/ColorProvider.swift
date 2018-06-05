//
//  ColorProvider.swift
//  PetClump
//
//  Created by YSH on 6/4/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class ColorProvider: UIColor{
    static var primaryColor: UIColor { return uicolorFromHex(rgbValue: 0x00cccc) }
    static var primaryDark:  UIColor { return uicolorFromHex(rgbValue: 0x3d3d5c) }
    static var colorAccent:  UIColor { return uicolorFromHex(rgbValue: 0xffb266) }
    
    
    static func uicolorFromHex(rgbValue:UInt32)->UIColor{
        let red = CGFloat((rgbValue & 0xFF0000) >> 16)/256.0
        let green = CGFloat((rgbValue & 0xFF00) >> 8)/256.0
        let blue = CGFloat(rgbValue & 0xFF)/256.0
        
        return UIColor(red:red, green:green, blue:blue, alpha:1.0)
    }
}
