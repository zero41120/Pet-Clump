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
    
    @IBOutlet weak var imageCollection: UICollectionView!
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
        self.setupImage()
    }
    
    // https://stackoverflow.com/questions/38529775/how-to-create-a-side-swiping-photo-gallery-in-swift-ios
    // https://stackoverflow.com/questions/26898955/adding-image-transition-animation-in-swift
    func setupImage(){
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
                recalculateIndex(direction: swipeGesture.direction)
                loadImage(imageIndex: imageIndex)
        }
    }
    
    
    func recalculateIndex(direction: UISwipeGestureRecognizerDirection){
        switch direction {
        case UISwipeGestureRecognizerDirection.left:
            imageIndex =
                imageIndex == imageUrls.count - 1 ?
                0 :
                imageIndex + 1
            
            
        case UISwipeGestureRecognizerDirection.right:
            imageIndex =
                imageIndex == imageUrls.count - 1 ?
                0 :
                imageIndex - 1
        default:
            break
        }
    }
    
    func loadImage(imageIndex: Int){
        if !images.indices.contains(imageIndex){
            UIImageView().load(url: imageUrls[imageIndex]) {
                self.images.append(self.imageView.image!)
            }
        }        
    }
}
