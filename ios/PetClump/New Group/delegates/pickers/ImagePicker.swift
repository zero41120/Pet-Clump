//
//  ImagePicker.swift
//  PetClump
//
//  Created by YSH on 5/6/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class ImagePicker: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    
    var imageView: UIImageView?
    var petProfile: PetProfile?
    
    init(imageView: UIImageView, profile: PetProfile){
        self.imageView = imageView
        self.petProfile = profile
        super.init()
    }
    
    func uploadImageToFirebaseStorage(originalImage: UIImage){
        //upload to firebase
        let data = compressImage(image: originalImage)
        let tag = imageView!.tag
        let fileName = NSUUID.init().uuidString + ".png"
        let storageRef = Storage.storage().reference(withPath: "image/\(fileName)")
        let uploadMetaData = StorageMetadata()
        uploadMetaData.contentType =  "image/png"
        storageRef.putData(data as Data, metadata: uploadMetaData) {
            (metadata, error) in if (error != nil) {
                print("I received an error! \(String(describing: error?.localizedDescription))")
            } else {
                storageRef.downloadURL(completion: { (url, error) in
                    if error != nil {
                        print("There is an error when uploading: \(error!)")
                    } else {
                        print("\(url!) for \(tag)")
                        self.imageView?.image = originalImage
                        switch tag {
                        case 0: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.main, url: "\(url!)")
                        case 1: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet1, url: "\(url!)")
                        case 2: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet2, url: "\(url!)")
                        case 3: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet3, url: "\(url!)")
                        case 4: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet4, url: "\(url!)")
                        case 5: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet5, url: "\(url!)")
                        case 6: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group1, url: "\(url!)")
                        case 7: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group2, url: "\(url!)")
                        case 8: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group3, url: "\(url!)")
                        default: break
                        }
                        self.petProfile?.upload(vc: nil, completion: nil)
                    }
                })
            }
        }
    }
    
    private func compressImage(image: UIImage) -> Data{
        let kb = 1024;
        let sizeLimitKb = 1000
        var size = image.highestQualityJPEGNSData.count
        if size < (sizeLimitKb * kb) { return image.highestQualityJPEGNSData }
        size = image.highQualityJPEGNSData.count
        if size < (sizeLimitKb * kb) { return image.highQualityJPEGNSData }
        size = image.mediumQualityJPEGNSData.count
        if size < (sizeLimitKb * kb) { return image.mediumQualityJPEGNSData }
        size = image.lowQualityJPEGNSData.count
        if size < (sizeLimitKb * kb) { return image.lowQualityJPEGNSData }
        return image.lowestQualityJPEGNSData
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        picker.dismiss(animated: true, completion: nil)
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        guard let _: String = info[UIImagePickerControllerMediaType] as? String else {
            picker.dismiss(animated: true, completion: nil)
            return
        }
        if let originalImage = info[UIImagePickerControllerOriginalImage] as? UIImage{
            uploadImageToFirebaseStorage(originalImage: originalImage)
        }
        picker.dismiss(animated: true, completion: nil)
        
    }
}
