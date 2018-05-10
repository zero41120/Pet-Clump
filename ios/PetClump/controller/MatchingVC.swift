//
//  MatchingVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth


class MatchingVC: UIViewController{
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        
        for view in self.view.subviews {
            if let image = view as? UIImageView {
                image.setRounded()
                PetProfileImageDownloader.init(uid: uid, sequence: image.tag, imageView: image).download()
                let tap = UITapGestureRecognizer(target: self, action: #selector(startMatching(sender:)))
                image.isUserInteractionEnabled = true
                image.addGestureRecognizer(tap)
            }
        }
    }
    
    @objc func startMatching(sender: UITapGestureRecognizer){
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "BestMatchingVC") as! BestMatchingVC
        pdv.petProfile = PetProfile()
        pdv.petProfile!.sequence = sender.view!.tag
        self.present(pdv, animated: true, completion: nil)
    }
}
