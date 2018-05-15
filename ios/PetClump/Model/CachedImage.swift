//
//  CachedImageDownloader.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import UIKit

class CachedImage {
    static let cache = NSCache<NSString, UIImage>()
    static private let instance = CachedImage()
    private init() { }
    
    static func getInstance() -> CachedImage {
        return instance
    }
    
    func download(url: String, completion: @escaping (UIImage)-> Void) {
        if let cachedImage = CachedImage.cache.object(forKey: NSString(string: url)) {
            print("Found cached image")
            completion(cachedImage)
        } else {
            DispatchQueue.global().async { () in
                if let data = try? Data(contentsOf: URL(string: url)!) {
                    if let image = UIImage(data: data) {
                        DispatchQueue.main.async {
                            CachedImage.cache.setObject(image, forKey: NSString(string: url))
                            completion(image)
                        } // dispatch
                    } // let image
                } // try data
            } // dispatch
        } // else
    }
}
