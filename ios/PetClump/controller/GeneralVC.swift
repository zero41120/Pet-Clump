//
//  GeneralVC.swift
//  PetClump
//
//  Created by YSH on 6/4/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class GeneralVC: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        for view in self.view.subviews {
            if let navigation = view as? UINavigationBar {
                // Bar background
                navigation.isTranslucent = false
                navigation.barTintColor = StyleProvider.primaryColor
                // Button color
                navigation.tintColor = UIColor.white
                // Title color
                navigation.titleTextAttributes =
                    [NSAttributedStringKey.foregroundColor : UIColor.white,
                     NSAttributedStringKey.font : StyleProvider.getDefaultFont(22)]
                continue
            }

            // Magic
            if view.responds(to: Selector("font")){
                let font = view.value(forKey: "font") as! UIFont
                let fontSize = font.pointSize
                view.setValue(StyleProvider.getDefaultFont(fontSize), forKey: "font")
                continue
            }
            
            if let button = view as? UIButton {
                let fontSize = button.titleLabel!.font.pointSize
                button.titleLabel?.font = StyleProvider.getDefaultFont(fontSize)
                continue
            }

            if let filler = view as? FillerView{
                filler.backgroundColor = StyleProvider.primaryColor
                continue
            }
        }
    }    
}

class FillerView: UIView {}
