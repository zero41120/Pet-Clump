//
//  ImageDownloader.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class ImageDownloader {
    func download(url: URL, completion: (()-> Void)? ) {
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
