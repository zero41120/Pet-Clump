//
//  ImagePicker.swift
//  PetClump
//
//  Created by YSH on 5/6/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class ImageInputDelegate: NSObject, UIImagePickerControllerDelegate, UINavigationControllerDelegate{
    
    var imageView: UIImageView?
    var petProfile: PetProfile?
    
    init(imageView: UIImageView, profile: PetProfile){
        self.imageView = imageView
        self.petProfile = profile
        super.init()
    }
    
    func uploadImageToFirebaseStorage(originalImage: UIImage){
        //upload to firebase
        let croppedImage = cropToBounds(image: originalImage, width: Double(imageView!.frame.width), height: Double(imageView!.frame.height))
        let data = compressImage(image: croppedImage)
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
                        let photoKey = PetProfile.tagToPhotoKey(tag: self.imageView!.tag)
                        // deletes old image
                        self.petProfile!.deleteImage(photoKey: photoKey)
                        // sets new image
                        self.imageView?.image = croppedImage
                        self.petProfile!.setPhotoUrl(key: photoKey, url: "\(url!)")
                        self.petProfile!.upload(vc: nil, completion: nil)
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
    
    
    //https://stackoverflow.com/questions/32041420
    func cropToBounds(image: UIImage, width: Double, height: Double) -> UIImage {
        
        let cgimage = image.cgImage!
        let contextImage: UIImage = UIImage(cgImage: cgimage)
        let contextSize: CGSize = contextImage.size
        var posX: CGFloat = 0.0
        var posY: CGFloat = 0.0
        var cgwidth: CGFloat = CGFloat(width)
        var cgheight: CGFloat = CGFloat(height)
        
        // See what size is longer and create the center off of that
        if contextSize.width > contextSize.height {
            posX = ((contextSize.width - contextSize.height) / 2)
            posY = 0
            cgwidth = contextSize.height
            cgheight = contextSize.height
        } else {
            posX = 0
            posY = ((contextSize.height - contextSize.width) / 2)
            cgwidth = contextSize.width
            cgheight = contextSize.width
        }
        
        let rect: CGRect = CGRect(x: posX, y: posY, width: cgwidth, height: cgheight)
        
        // Create bitmap image from context using the rect
        let imageRef: CGImage = cgimage.cropping(to: rect)!
        
        // Create a new image based on the imageRef and rotate back to the original orientation
        let image: UIImage = UIImage(cgImage: imageRef, scale: image.scale, orientation: image.imageOrientation)
        
        return image
    }
}
