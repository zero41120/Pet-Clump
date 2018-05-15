//
//  ImageScrollerView.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//
//

import UIKit


class ImageScrollerView: UIScrollView {
    
    func setupScrollerWithImages(images : [UIImage]) {
        var x : CGFloat = 0.0
        let y : CGFloat = 0.0
        var index : CGFloat = 0
        self.showsHorizontalScrollIndicator = false
        self.isPagingEnabled = true
        self.contentSize = CGSize(width: CGFloat(images.count) * self.frame.size.width, height: self.frame.height)
        for image in images{
            let imageView = UIImageView(frame: CGRect(x: x, y: y, width: self.frame.width, height: self.frame.height))
            imageView.image = image
            self.addSubview(imageView)
            index = index + 1
            x = self.frame.width * index
        }
    }
}

