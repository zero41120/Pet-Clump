//
//  ImageViewExtension.swift
//  PetClump
//
//  Created by YSH on 4/28/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

extension UIImageView {
    static func getDefaultSelectedColor() -> UIColor{
        return UIColor(red:25/255, green:225/255, blue:25/255, alpha: 1)
    }
    static func getDefaultDeselectedColor() -> UIColor{
        return UIColor(red:25/255, green:25/255, blue:25/255, alpha: 0.7)
    }
    
    func setRounded() {
        let radius = self.frame.width / 2
        self.layer.cornerRadius = radius
        self.layer.masksToBounds = true
    }
    
    func load(url: String){
        if url == "" {
            self.image = nil
            return
        }
        CachedImage.getInstance().download(url: url) { (image) in
            self.image = image
        }
    }
    
    func load(url: String, completion: (()-> Void)?) {
        CachedImage.getInstance().download(url: url) { (image) in
            self.image = image
            if completion != nil{
                completion!()
            }
        }
    }
}
