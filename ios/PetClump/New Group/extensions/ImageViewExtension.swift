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
    
    func load(url: URL) {
        DispatchQueue.global().async { [weak self] in
            if let data = try? Data(contentsOf: url) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        self?.image = image
                    }
                }
            }
        }
    }
}
