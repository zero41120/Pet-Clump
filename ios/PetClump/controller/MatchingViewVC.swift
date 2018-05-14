//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth

class MatchingViewVC: UIViewController{
    
    // Assigned by caller view
    var petProfile: PetProfile?
    
    @IBOutlet weak var imageView: UIImageView!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var specieLabel: UILabel!
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var bioTextField: UITextView!
    
    var imageIndex = 0
    var imageUrls: [String] = []
    var images: [UIImage] = []
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        nameLabel.text = petProfile!.name
        specieLabel.text = petProfile!.specie
        ageLabel.text = petProfile!.age
        bioTextField.text = petProfile!.bio
        self.loadImages()
    }
    
    // https://stackoverflow.com/questions/38529775/how-to-create-a-side-swiping-photo-gallery-in-swift-ios
    // https://stackoverflow.com/questions/26898955/adding-image-transition-animation-in-swift
    func loadImages(){
        imageUrls = petProfile!.getPhotoUrls(isPulic: true)
        imageView.load(url: imageUrls[imageIndex]) {
            self.images.append(self.imageView.image!)
        }
        let swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(swipeOnImage(sender:)))
        swipeRight.direction = UISwipeGestureRecognizerDirection.right
        self.view.addGestureRecognizer(swipeRight)
        let swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(swipeOnImage(sender:)))
        swipeLeft.direction = UISwipeGestureRecognizerDirection.left
        self.view.addGestureRecognizer(swipeLeft)
        
    }
    
    @objc func swipeOnImage(sender: UIGestureRecognizer){
        if let swipeGesture = sender as? UISwipeGestureRecognizer {
            switch swipeGesture.direction {
                case UISwipeGestureRecognizerDirection.left:
                    if imageIndex == imageUrls.count - 1 {
                        imageIndex = 0
                    } else {
                        imageIndex += 1
                    }
                    if images.indices.contains(imageIndex){
                        imageView.image = images[imageIndex]
                    } else {
                        imageView.load(url: imageUrls[imageIndex]) {
                            self.images.append(self.imageView.image!)
                        }
                    }
                
                case UISwipeGestureRecognizerDirection.right:
                    if imageIndex == 0 {
                        imageIndex = imageUrls.count - 1
                    }else{
                        imageIndex -= 1
                    }
                    if images.indices.contains(imageIndex){
                        imageView.image = images[imageIndex]
                    } else {
                        imageView.load(url: imageUrls[imageIndex]) {
                            self.images.append(self.imageView.image!)
                        }
                    }
                default: break
            }
        }
    }
}