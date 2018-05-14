//
//  ImageDownloader.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class ImageDownloader {
    func download(urls: [String], completion: @escaping ([UIImage])-> Void ) {
        DispatchQueue.global().async { [weak self] in
            var images: [UIImage] = []
            for url in urls{
                print("\(url) start")
                if let data = try? Data(contentsOf: URL(string: url)!) {
                    if let image = UIImage(data: data) {
                        DispatchQueue.main.async {
                            print("\(url) done")
                            images.append(image)
                        }
                    }
                }
            }
            print("going to call complete")
            completion(images)
        }
    }
}
