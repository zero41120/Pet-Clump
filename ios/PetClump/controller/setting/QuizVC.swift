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

class QuizVC: UIViewController{

    // Assigned by caller
    var petProfile: PetProfile?
    var swipeableView: ZLSwipeableView?
    
    let colors = ["Turquoise", "Green Sea", "Emerald", "Nephritis", "Peter River",
                  "Belize Hole", "Amethyst", "Wisteria", "Wet Asphalt", "Midnight Blue"]
    var questions: [String]?
    var answers:   [String] = []
    var colorIndex = 0
   
    override func viewDidLoad() {
        guard let _ = Auth.auth().currentUser?.uid else { return }
        super.viewDidLoad()
        petProfile!.download {
            self.didCompleteDownload()
        }
        // didCompleteDownloader will setup quiz action
    }
    
    func didCompleteDownload() {
        print("profile downloaded, ready for quiz!")
        setupQuiz()
    }
    
    private func setupQuiz(){
        view.clipsToBounds = true
        swipeableView = ZLSwipeableView()
        view.addSubview(swipeableView!)
        swipeableView!.didSwipe = {view, direction, vector in
            print("Did swipe view in direction: \(direction), vector: \(vector)")
            typealias s = ZLSwipeableViewDirection
            switch direction {
                case s.Left:  self.answers.append(QuizAnswer.NO)
                case s.Right: self.answers.append(QuizAnswer.YES)
                default:      self.answers.append(QuizAnswer.SKIP)
            }
            // TODO pop up something to notify user
            if self.answers.count == 10 {
                print("User did finish quiz")
                self.petProfile!.quiz += self.answers.joined(separator: "")
                self.petProfile!.upload(vc: self){
                    self.didCompleteUpload()
                }
            }
        }
    
        constrain(swipeableView!, view) { view1, view2 in
            view1.left == view2.left+50
            view1.right == view2.right-50
            view1.top == view2.top + 120
            view1.bottom == view2.bottom - 100
        }
        
        //give out the quiz questions to the user based their previously answered question index.
        self.questions = QuizQuestion.getQuestion(quizString: (petProfile?.quiz)!, count: 10)
        
        swipeableView!.interpretDirection = {(topView: UIView, direction: ZLSwipeableViewDirection,
            views: [UIView], swipeableView: ZLSwipeableView) in
            let swipeVelocity = CGFloat(500)
            let location = CGPoint(x: topView.center.x-30, y: topView.center.y*0.1)
            var directionVector: CGVector?
            typealias s = ZLSwipeableViewDirection
            switch direction {
                case s.Left:  directionVector = CGVector(dx: -swipeVelocity, dy: 0)
                case s.Right: directionVector = CGVector(dx: swipeVelocity, dy: 0)
                case s.Up:    directionVector = CGVector(dx: 0, dy: -swipeVelocity)
                case s.Down:  directionVector = CGVector(dx: 0, dy: swipeVelocity)
                default:      directionVector = CGVector(dx: 0, dy: 0)
            }
            return (location, directionVector!)
        }
    }
    
    override func viewDidLayoutSubviews() {
        super.viewDidLayoutSubviews()
        guard (swipeableView != nil) else { return }
        swipeableView!.nextView = {
            return self.nextCardView()
        }
    }
    
    func didCompleteUpload() {
        print("Uploaded quiz result!")
        self.dismiss(animated: true, completion: nil)
    }
    
    
    func nextCardView() -> UIView? {
        guard colorIndex < colors.count else { return nil }
        
        let cardView = CardView(frame: swipeableView!.bounds)
        cardView.backgroundColor = colorForName(colors[colorIndex])
        colorIndex += 1
        
        // TODO make the text larget and at the center of the card view
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: 200, height: 70))
        label.center = CGPoint(x: 125, y: 300)
        label.numberOfLines = 0

        label.textAlignment = .center
        label.text = questions![colorIndex - 1]
        cardView.addSubview(label)
        return cardView
    }
    
    func colorForName(_ name: String) -> UIColor {
        let sanitizedName = name.replacingOccurrences(of: " ", with: "")
        let selector = "flat\(sanitizedName)Color"
        return UIColor.perform(Selector(selector)).takeUnretainedValue() as! UIColor
    }
    
    @IBAction func tapExit(_ sender: Any) {
        let title = NSLocalizedString("Leaving Quiz", comment: "This is the title on an alert to notify user when they click Exit before answering all quiz questions")
        let message = NSLocalizedString("Are you sure you want to leave the quiz? If you leave now, the answers will not be saved.", comment: "This is the message on an alert to notify user when they click Exit before answering all quiz questions")
        confirmBeforeDismiss(title: title, message: message)
    }
}
