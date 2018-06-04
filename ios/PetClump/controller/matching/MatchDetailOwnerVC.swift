//
//  MatchDetailOwnerVC.swift
//  PetClump
//
//  Created by YSH on 6/4/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class MatchDetailOwnerVC: UIViewController{
        
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var genderLabel: UILabel!
    @IBOutlet weak var imageScroller: ImageScrollerView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        
        let friendPet = MatchTabBar.thatPet!
        let friendOwnerId = friendPet.ownerId
        let _ = OwnerProfile(id: friendOwnerId, completion: { (friendOwer) in
            self.ageLabel.text = friendOwer.getAgeString()
            self.nameLabel.text = friendOwer.name
            self.genderLabel.text = friendOwer.gender
            self.imageScroller.setupScrollerWith(urls: friendPet.getGroupPhotoUrls())
            // let commonTime = friendOwer.freeTime.getCommonFreeTime(other: )
            
            // Gets weekly schedule
//            let color = UIColor(red:222/255, green:225/255, blue:227/255, alpha: 1).cgColor
//            for view in self.view.subviews as [UIView] {
//                if let imageView = view as? UIImageView {
//                    let week = imageView.tag / 10 - 1
//                    let part = imageView.tag % 10 - 1
//                    imageView.layer.borderColor = color
//                    imageView.layer.borderWidth = 1
//                    imageView.backgroundColor = profile.freeTime.isFree(weekDay: week, partDay: part) ?
//                        UIImageView.getDefaultSelectedColor() :
//                        UIImageView.getDefaultDeselectedColor()
//                    
//                    let tap = UITapGestureRecognizer(target: self, action: #selector(toggleImageColor(tapGestureRecognizer:)))
//                    imageView.isUserInteractionEnabled = true
//                    imageView.addGestureRecognizer(tap)
//                }
//            }
        })
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
