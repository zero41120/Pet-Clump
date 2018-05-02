//
//  QuizVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase
import UIColor_FlatColors
import Cartography



class QuizVC: UIViewController, ProfileDownloader, ProfileUploader{

    var petProfile: PetProfile?
    var swipeableView: ZLSwipeableView?
    
    var colors = ["Turquoise", "Green Sea", "Emerald", "Nephritis", "Peter River",
                  "Belize Hole", "Amethyst", "Wisteria", "Wet Asphalt", "Midnight Blue"]
    var questions = ["Q1", "Q2","Q3", "Q4","Q5", "Q6","Q7", "Q8","Q9", "Q10"]
    var answer = [Int]()
    
    var questioncount = 0
    var colorIndex = 0
   
    override func viewDidLoad() {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        petProfile!.download(uid: uid, completion: self)
        // didCompleteDownloader will setup quiz action
    }
 
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        guard (swipeableView != nil) else { return }
        swipeableView!.nextView = {
            return self.nextCardView()
        }
    }
    
    func didCompleteDownload() {
        print("profile downloaded, ready for quiz!")
        self.questions = Array(QuizQuestion.defaultQuestions[0...10])
        
        view.clipsToBounds = true
        swipeableView = ZLSwipeableView()
        view.addSubview(swipeableView!)
        swipeableView!.didSwipe = {view, direction, vector in
            self.answer.append(Int(direction.rawValue))
            print("Did swipe view in direction: \(direction), vector: \(vector)")
        }
        
        constrain(swipeableView!, view) { view1, view2 in
            view1.left == view2.left+50
            view1.right == view2.right-50
            view1.top == view2.top + 120
            view1.bottom == view2.bottom - 100
        }
        
        // change how ZLSwipeableViewDirection gets interpreted to location and direction
        swipeableView!.interpretDirection = {(topView: UIView, direction: ZLSwipeableViewDirection, views: [UIView], swipeableView: ZLSwipeableView) in
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
    
    
    func nextCardView() -> UIView? {
        if colorIndex >= colors.count {
            return nil
        }
        
        let cardView = CardView(frame: swipeableView!.bounds)
        cardView.backgroundColor = colorForName(colors[colorIndex])
        colorIndex += 1
        questioncount += 1
        
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 21))
        label.center = CGPoint(x: 20, y: 30)
        label.textAlignment = .center
        label.text = questions[colorIndex - 1]
        cardView.addSubview(label)
        return cardView
    }
    
    func colorForName(_ name: String) -> UIColor {
        let sanitizedName = name.replacingOccurrences(of: " ", with: "")
        let selector = "flat\(sanitizedName)Color"
        return UIColor.perform(Selector(selector)).takeUnretainedValue() as! UIColor
    }
    
//    @objc func leftBarButtonAction() {
//        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 10, y: 300), inDirection: CGVector(dx: -700, dy: -300))
//    }
//
//    @objc func upBarButtonAction() {
//        self.swipeableView.swipeTopView(fromPoint: CGPoint(x: 100, y: 30), inDirection: CGVector(dx: 100, dy: -800))
//    }
//
    @IBAction func tapExit(_ sender: Any) {
        let title = NSLocalizedString("Leaving Quiz", comment: "This is the title on an alert to notify user when they click Exit before answering all quiz questions")
        let message = NSLocalizedString("Are you sure you want to leave the quiz? If you leave now, the answers will not be saved.", comment: "This is the message on an alert to notify user when they click Exit before answering all quiz questions")
        confirmBeforeDismiss(title: title, message: message)
    }
}
