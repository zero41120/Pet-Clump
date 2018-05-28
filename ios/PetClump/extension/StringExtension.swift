//
//  StringExtension.swift
//  PetClump
//
//  Created by YSH on 5/16/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit


extension String{
    
    func urlEscape() -> String{
        return self.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!
    }
    
    func estimateWidth(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.width
    }
    
    func estimateHeight(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.height
    }
    
    func getCGSize(usingFont font: UIFont) -> CGSize {
        let fontAttributes = [NSAttributedStringKey.font: font]
        return self.size(withAttributes: fontAttributes)
    }
}
