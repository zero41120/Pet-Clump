//
//  QuizVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class QuizVC: ZLSwipeableViewController, ProfileDownloader, ProfileUploader{

    var petProfile: PetProfile?
   
    override func viewDidLoad() {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        petProfile!.download(uid: uid, completion: self)
        // didCompleteDownloader will setup quiz action
    }
 
    
    func didCompleteDownload() {
        print("profile downloaded, ready for quiz!")
        leftBarButtonItem.action = #selector(leftBarButtonAction)
        upBarButtonItem.action   = #selector(upBarButtonAction)
        self.questions = QuizQuestion.defaultQuestions
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
    
    // MARK: - Actions
    
    @objc func leftBarButtonAction() {
        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 10, y: 300), inDirection: CGVector(dx: -700, dy: -300))
    }
    
    @objc func upBarButtonAction() {
        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 100, y: 30), inDirection: CGVector(dx: 100, dy: -800))
    }
    
    @IBAction func tapExit(_ sender: Any) {
        let title = NSLocalizedString("Leaving Quiz", comment: "This is the title on an alert to notify user when they click Exit before answering all quiz questions")
        let message = NSLocalizedString("Are you sure you want to leave the quiz? If you leave now, the answers will not be saved.", comment: "This is the message on an alert to notify user when they click Exit before answering all quiz questions")
        confirmBeforeDismiss(title: title, message: message)
    }
}
