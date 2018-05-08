//
//  File.swift
//  PetClump
//
//  Created by YSH on 5/7/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class PetProfileImageDownloader: ProfileDownloader{
    private let imageView: UIImageView
    private var petProfile: PetProfile?
    private let uid: String
    private let sequence: Int
    
    init(uid: String, sequence: Int, imageView: UIImageView){
        self.imageView = imageView
        self.uid = uid
        self.sequence = sequence
    }
    
    func download(){
        petProfile = PetProfile()
        petProfile!.sequence = self.sequence
        petProfile!.download(uid: self.uid, completion: self)
    }
    
    func didCompleteDownload() {
        let url = petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
        if url == ""{
            imageView.backgroundColor = UIImageView.getDefaultSelectedColor()
        } else {
            imageView.load(url: url)
        }
    }
}
