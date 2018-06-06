//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class MatchDetailVC: GeneralVC{
    
    var friendHandler: FriendHandler?
    
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var specieLabel: UILabel!
    @IBOutlet weak var bioTextField: UITextView!
    @IBOutlet weak var imageScroller: ImageScrollerView!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet weak var reportButton: UIBarButtonItem!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        let friendProfile = MatchTabBar.thatPet
        let myProfile = MatchTabBar.thisPet
        ageLabel.text = friendProfile!.age
        nameLabel.text = friendProfile!.name
        specieLabel.text = friendProfile!.specie
        bioTextField.text = friendProfile!.bio
        friendHandler = FriendHandler(myProfile: myProfile!, friendProfile: friendProfile!)
        imageScroller.setupScrollerWith(urls: friendProfile!.getPetPhotoUrls())
        friendHandler!.shouldDisableAddFriendButton(ifTrue: {

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
        addButton.title = NSLocalizedString("Added", comment: "This is the replacement text on the 'Add Friend' button when a friend request is sent/received")
    }
    
    @IBAction func tapReport(_ sender: Any) {
        // Action sheet to prompt user to upload / delete photo
        let docRef = Firestore.firestore().collection("reports").document()
        let thisId = friendHandler!.myPet.getId()
        let thatId = friendHandler!.friendPet.getId()
        let alert = UIAlertController(title: NSLocalizedString("Report", comment: "This is an alert title when user clicks on the image to upload or delete"), message: "", preferredStyle: UIAlertControllerStyle.actionSheet)
        // Inappropriate image
        alert.addAction(UIAlertAction(title: "Inappropriate image", style: .destructive, handler: { (action: UIAlertAction!) in
            let data = ["from" : thisId, "to": thatId, "reason" : action.title!]
            docRef.setData(data)
        }))
        // Offensive Language
        alert.addAction(UIAlertAction(title: "Offensive Language", style: .destructive, handler: { (action: UIAlertAction!) in
            let data = ["from" : thisId, "to": thatId, "reason" : action.title!]
            docRef.setData(data)
        }))
        // Non-pet related activie
        alert.addAction(UIAlertAction(title: "Non-pet related activie", style: .destructive, handler: { (action: UIAlertAction!) in
            let data = ["from" : thisId, "to": thatId, "reason" : action.title!]
            docRef.setData(data)
        }))
        // Cancel
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        self.present(alert, animated: true)
        
        }
}
