//
//  File.swift
//  PetClump
//
//  Created by YSH on 5/7/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class PetProfileImagexDownloader{
    private let imageView: UIImageView
    var petProfile: PetProfile?
    private let uid: String
    private let sequence: Int
    
    init(uid: String, sequence: Int, imageView: UIImageView){
        self.imageView = imageView
        self.uid = uid
        self.sequence = sequence
    }
    
    func download(){
        petProfile = PetProfile(uid: uid, sequence: sequence) { profile in
            let url = profile.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
            if url == ""{
                self.imageView.backgroundColor = UIImageView.getDefaultDeselectedColor()
            } else {
                self.imageView.load(url: url)
            }
        }
    }
}
