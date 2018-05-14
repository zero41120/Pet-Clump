//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth
import BubblePictures

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
    var configs: [BPCellConfigFile] = []
    private var bubblePictures: BubblePictures!

    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
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
        let layoutConfigurator = BPLayoutConfigurator(
            backgroundColorForTruncatedBubble: UIColor.gray,
            fontForBubbleTitles: UIFont(name: "HelveticaNeue-Light", size: 16.0)!,
            colorForBubbleBorders: UIColor.white,
            colorForBubbleTitles: UIColor.white,
            maxCharactersForBubbleTitles: 2,
            maxNumberOfBubbles: 2,
            direction: .leftToRight,
            alignment: .center)
        
        imageUrls = petProfile!.getPhotoUrls(isPulic: true)
        for url in imageUrls {
            var config = BPCellConfigFile(imageType: BPImageType.URL(URL(string: url)!), title: "x")
            self.configs.append(config)
        }
//        ImageDownloader().download(urls: imageUrls) { (images) in
//            for image in images {
//                print("image in")
//                self.images.append(image)
//                self.configs.append(BPCellConfigFile(imageType: BPImageType.image(image), title: ""))
//            }
//
//        }
        
        bubblePictures = BubblePictures(collectionView: imageCollection, configFiles: configs, layoutConfigurator: layoutConfigurator)
        bubblePictures.delegate = self

        
        
//
//
//        let swipeRight = UISwipeGestureRecognizer(target: self, action: #selector(swipeOnImage(sender:)))
//        swipeRight.direction = UISwipeGestureRecognizerDirection.right
//        self.view.addGestureRecognizer(swipeRight)
//        let swipeLeft = UISwipeGestureRecognizer(target: self, action: #selector(swipeOnImage(sender:)))
//        swipeLeft.direction = UISwipeGestureRecognizerDirection.left
//        self.view.addGestureRecognizer(swipeLeft)
        
    }
    
    @objc func swipeOnImage(sender: UIGestureRecognizer){
        if let swipeGesture = sender as? UISwipeGestureRecognizer {
                recalculateIndex(direction: swipeGesture.direction)
                print("\(swipeGesture.direction)")
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
}

extension MatchingViewVC: BPDelegate {
    func didSelectTruncatedBubble() {
        print("Selected truncated bubble")
    }
    
<<<<<<< HEAD
    func didSelectBubble(at index: Int) {
        print(index)
=======
    func loadImage(imageIndex: Int){
        if !images.indices.contains(imageIndex){
            imageView.load(url: imageUrls[imageIndex]) {
                self.images.append(self.imageView.image!)
            }
        }        
>>>>>>> parent of 6f0f952... Adds BP pic swipe lib
    }
}
