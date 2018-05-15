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
    
    func setupScrollerWith(urls: [String]){
        self.showsHorizontalScrollIndicator = false
        self.isPagingEnabled = true
        
        let width = self.frame.width
        let scrollWidth = CGFloat(urls.count) * width
        let height = self.frame.height

        self.contentSize = CGSize(width: scrollWidth, height: height)
        
        var x : CGFloat = 0.0
        let y : CGFloat = 0.0
        for (index, url) in urls.enumerated() {
            x = self.frame.width * CGFloat(index)
            let imageView = UIImageView(frame: CGRect(x: x, y: y, width: width, height: height))
            imageView.load(url: url)
            self.addSubview(imageView)
        }
    }
}

