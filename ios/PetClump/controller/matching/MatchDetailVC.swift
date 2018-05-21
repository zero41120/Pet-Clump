//
//  MatchingViewVC.swift
//  PetClump
//
//  Created by YSH on 5/14/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth

class MatchDetailVC: UIViewController{
    
    // Assigned by caller view
    var friendProfile: PetProfile?
    var myProfile: PetProfile?
    
    @IBOutlet weak var ageLabel: UILabel!
    @IBOutlet weak var nameLabel: UILabel!
    @IBOutlet weak var specieLabel: UILabel!
    @IBOutlet weak var bioTextField: UITextView!
    @IBOutlet weak var imageScroller: ImageScrollerView!
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    @IBAction func tapAddFriend(_ sender: Any) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "ChatRoomVC") as! ChatRoomVC
        pdv.friendPetProfile = friendProfile!
        pdv.myPetProfile = myProfile!
        self.present(pdv, animated: true, completion: nil)
    }
    
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
    }
}
