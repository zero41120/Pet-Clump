//
//  GeneralVC.swift
//  PetClump
//
//  Created by YSH on 6/4/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class GeneralVC: UIViewController {
    var primaryColor: UIColor { return self.uicolorFromHex(rgbValue: 0x00cccc) }
    var primaryDark:  UIColor { return self.uicolorFromHex(rgbValue: 0x3d3d5c) }
    var colorAccent:  UIColor { return self.uicolorFromHex(rgbValue: 0xffb266)}
    override func viewDidLoad() {
        super.viewDidLoad()
        for view in self.view.subviews {
            if let navigation = view as? UINavigationBar {
                // Bar background
                navigation.barTintColor = self.primaryColor
                // Button color
                navigation.tintColor = UIColor.white
                // Title color
                navigation.titleTextAttributes = [NSAttributedStringKey.foregroundColor : UIColor.white]
            }
            
            if let filler = view as? FillerView{
                filler.backgroundColor = self.primaryColor
            }
        }
    }    
}

class FillerView: UIView {}
