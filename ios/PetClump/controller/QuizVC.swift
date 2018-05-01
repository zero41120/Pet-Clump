//
//  QuizVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class QuizVC: ZLSwipeableViewController, ConfirmDismissAlert, ProfileDownloader, ProfileUploader{
    func confirmBeforeDismiss(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        alert.addAction(UIAlertAction(title: "Leave", style: .destructive, handler: { (action: UIAlertAction!) in
            self.dismiss(animated: true, completion: nil)
        }))
        
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        
        self.present(alert, animated: true, completion: nil)
    }
    

    var petProfile: PetProfile?
   
    @IBAction func tapExit(_ sender: Any) {
        confirmBeforeDismiss(title: "Leaving quiz", message: "Are you sure you want to leave the quiz?")
    }
    
    override func viewDidLoad() {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        petProfile!.download(uid: uid, completion: self)

        // didCompleteDownloader will setup quiz action
        
    }
 
    // MARK: - Actions
    
    @objc func leftBarButtonAction() {
        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 10, y: 300), inDirection: CGVector(dx: -700, dy: -300))
    }
    
    @objc func upBarButtonAction() {
        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 100, y: 30), inDirection: CGVector(dx: 100, dy: -800))
    }
    
    func didCompleteDownload() {
        print("profile downloaded, ready for quiz!")
        leftBarButtonItem.action = #selector(leftBarButtonAction)
        upBarButtonItem.action = #selector(upBarButtonAction)
        
        // change how ZLSwipeableViewDirection gets interpreted to location and direction
        swipeableView.interpretDirection = {(topView: UIView, direction: ZLSwipeableViewDirection, views: [UIView], swipeableView: ZLSwipeableView) in
            let programmaticSwipeVelocity = CGFloat(500)
            let location = CGPoint(x: topView.center.x-30, y: topView.center.y*0.1)
            var directionVector: CGVector?
            switch direction {
            case ZLSwipeableViewDirection.Left:
                directionVector = CGVector(dx: -programmaticSwipeVelocity, dy: 0)
            case ZLSwipeableViewDirection.Right:
                directionVector = CGVector(dx: programmaticSwipeVelocity, dy: 0)
            case ZLSwipeableViewDirection.Up:
                directionVector = CGVector(dx: 0, dy: -programmaticSwipeVelocity)
            case ZLSwipeableViewDirection.Down:
                directionVector = CGVector(dx: 0, dy: programmaticSwipeVelocity)
            default:
                directionVector = CGVector(dx: 0, dy: 0)
            }
            return (location, directionVector!)
        }
    }
    
    func didCompleteUpload() {
        self.dismiss(animated: true, completion: nil)
    }
    
    
}
