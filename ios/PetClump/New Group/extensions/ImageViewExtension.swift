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
    
    func setShadow() {
        layer.shadowColor = UIColor.black.cgColor
        layer.shadowOpacity = 0.25
        layer.shadowOffset = CGSize(width: 0, height: 1.5)
        layer.shadowRadius = 4.0
        layer.shouldRasterize = true
        layer.rasterizationScale = UIScreen.main.scale
    }
    
    func load(url: String){
        if url == "" {
            self.image = nil
            return
        }
        load(url: url, completion: nil)
    }
    
    func load(url: String, completion: (()-> Void)?) {
        load(url: URL(string: url)!, completion: completion)
    }
    
    func load(url: URL, completion: (()-> Void)? ) {
        DispatchQueue.global().async { [weak self] in
            if let data = try? Data(contentsOf: url) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        self?.image = image
                        if completion != nil {
                            completion!()
                        }
                    }
                }
            }
        }
    }
}
