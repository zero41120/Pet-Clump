//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class MatchDetailVC: UIViewController{
    
    // Assigned by caller view
    var friendProfile: PetProfile?
    var myProfile: PetProfile?
    var friendHandler: FriendHandler?
    
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var specieLabel: UILabel!
    @IBOutlet weak var bioTextField: UITextView!
    @IBOutlet weak var imageScroller: ImageScrollerView!
    @IBOutlet weak var addButton: UIBarButtonItem!

    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        ageLabel.text = friendProfile!.age
        nameLabel.text = friendProfile!.name
        specieLabel.text = friendProfile!.specie
        bioTextField.text = friendProfile!.bio
        let imageUrls = friendProfile!.getPhotoUrls(isPulic: true)
        self.imageScroller.setupScrollerWith(urls: imageUrls)
        friendHandler = FriendHandler(myProfile: myProfile!, friendProfile: friendProfile!, caller: self)
        friendHandler!.isPending(ifTrue: {
            print("Disabled add friend due to pending")
            self.disableAddButton()
        }, ifFalse: nil)
        
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func tapAddFriend(_ sender: Any) {
        friendHandler?.addFriend(){
            disableAddButton()
        }
    }
    
    func disableAddButton() {
        self.addButton.isEnabled = false
        addButton.title = NSLocalizedString("Pending", comment: "This is the replacement text on the 'Add Friend' button when a friend request is sent/received")
    }
}
