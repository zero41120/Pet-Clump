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
    static private let cache = NSCache<NSString, UIImage>()
    static private let instance = CachedImage()
    private init() { }
    
    static func getInstance() -> CachedImage {
        return instance
    }
    
    func download(url: String, completion: @escaping (UIImage)-> Void) {
        // Check cache
        if let cachedImage = CachedImage.cache.object(forKey: NSString(string: url)) {
            completion(cachedImage)
            return
        }
        // Check disk
        if let storedImage = readFromDick(url: url) {
            CachedImage.cache.setObject(storedImage, forKey: NSString(string: url))
            completion(storedImage)
            return
        }
      
        // Download from web
        DispatchQueue.global().async { () in
            if let data = try? Data(contentsOf: URL(string: url)!) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        CachedImage.cache.setObject(image, forKey: NSString(string: url))
                        self.writeToDisk(image: image, url: url)
                        completion(image)
                    } // dispatch
                } // let image
            } // try data
        } // dispatch
    }
    
    private func writeToDisk(image: UIImage, url: String){
        do {
            let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
            let fileURL = documentsURL.appendingPathComponent(url.urlEscape())
            if let pngImageData = UIImagePNGRepresentation(image) {
                try pngImageData.write(to: fileURL, options: .atomic)
            }
        } catch { }
    }
    
    private func readFromDick(url: String) -> UIImage?{
        let documentsURL = FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
        let filePath = documentsURL.appendingPathComponent(url.urlEscape()).path
        if FileManager.default.fileExists(atPath: filePath) {
            return UIImage(contentsOfFile: filePath)
        } else {
            return nil
        }
    }
}
