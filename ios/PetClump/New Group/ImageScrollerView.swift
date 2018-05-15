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
        
        let height = self.frame.height
        let width = self.frame.width
        let scrollWidth = CGFloat(urls.count) * width

        self.contentSize = CGSize(width: scrollWidth, height: height)
        
        for (index, url) in urls.enumerated() {
            let x = self.frame.width * CGFloat(index)
            let imageView = UIImageView(frame: CGRect(x: x, y: 0.0, width: width, height: height))
            imageView.load(url: url)
            self.addSubview(imageView)
        }
    }
}

