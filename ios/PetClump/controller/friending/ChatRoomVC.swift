//
//  ChatRoomVC.swift
//  PetClump
//
//  Created by YSH on 5/18/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class ChatRoomVC: UIViewController, UITextFieldDelegate, UITableViewDelegate, UITableViewDataSource {
    
    private static let MAX_WIDTH = 200
    private static let MIN_HEIGHT = 96
    
    @IBOutlet weak var navBar: UINavigationBar!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var inputField: UITextField!
    @IBOutlet weak var bottomConstraint: NSLayoutConstraint!
    
    // Assign by caller
    var friendPetProfile: PetProfile?
    var myPetProfile: PetProfile? = PetProfile.most_recent_pet
    
    var messages: [Message] = []
    var messenger: Messenger?

    
    override func viewDidLoad() {
        super.viewDidLoad()
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardNotification), name: NSNotification.Name.UIKeyboardWillChangeFrame, object: nil)
        tableView.register(MessageCell.self, forCellReuseIdentifier: "cell")
        tableView.separatorColor = UIColor.clear
        tableView.delegate = self
        tableView.dataSource = self
        tableView.keyboardDismissMode = .onDrag

        sendButton.addTarget(self, action: #selector(handleSend), for: .touchUpInside)
        inputField.delegate = self
        
        // TODO Download Message
        messenger = Messenger(myPet: myPetProfile!, friendPet: friendPetProfile!)
        messenger!.startListen { (messages) in
            self.messages = messages
            self.tableView.reloadData()
            self.scrollBottom(animated: true)
        }
    }
    
    // https://stackoverflow.com/questions/25693130
    @objc func keyboardNotification(notification: NSNotification) {
        if let userInfo = notification.userInfo {
            let endFrame = (userInfo[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue
            let endFrameY = endFrame?.origin.y ?? 0
            let duration:TimeInterval = (userInfo[UIKeyboardAnimationDurationUserInfoKey] as? NSNumber)?.doubleValue ?? 0
            let animationCurveRawNSN = userInfo[UIKeyboardAnimationCurveUserInfoKey] as? NSNumber
            let animationCurveRaw = animationCurveRawNSN?.uintValue ?? UIViewAnimationOptions.curveEaseInOut.rawValue
            let animationCurve:UIViewAnimationOptions = UIViewAnimationOptions(rawValue: animationCurveRaw)
            if endFrameY >= UIScreen.main.bounds.size.height {
                self.bottomConstraint?.constant = 0.0
            } else {
                self.bottomConstraint?.constant = -(endFrame?.size.height ?? 0.0) + 36
            }
            UIView.animate(withDuration: duration,
                           delay: TimeInterval(0),
                           options: animationCurve,
                           animations: {
                            self.view.layoutIfNeeded()
                            self.scrollBottom(animated: true)
                            },
                           completion: nil)
        }
    }
    
    @objc func handleSend() {
        guard self.inputField!.text != ""  else { return }
        messenger?.upload(message: self.inputField!.text!, completion: { (message) in
            self.inputField!.text = ""
        })
        
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        handleSend()
        return true
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        // Cell
        let senderId = messages[indexPath.row].senderId
        let senderCellId = senderId == myPetProfile?.getId() ? MessageCell.myCellId : MessageCell.friendCellId
        let cell = tableView.dequeueReusableCell(withIdentifier: senderCellId) as! MessageCell
        
        // Text field
        let message = messages[indexPath.row].message
        cell.textField.text = message
        cell.textField.frame = estimateFrameForText(text: message)
        cell.textField.topAnchor.constraint(equalTo: cell.containerView.topAnchor, constant: 8)
        cell.textField.rightAnchor.constraint(equalTo: cell.petImage.rightAnchor, constant: 8)
        cell.textField.contentInset = UIEdgeInsetsMake(1, 8, 1, 8);
        cell.makeBubble(backColor: UIColor(red: 0, green: 137/255, blue: 249/255, alpha: 1), textColor: UIColor.white)
        
        // Image
        let senderImageUrl =
            senderId == myPetProfile?.getId() ?
            myPetProfile?.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main) :
            friendPetProfile?.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
        cell.petImage.load(url: senderImageUrl!)
        
        // Time
        let time = messages[indexPath.row].time
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "hh:mm"
        cell.timeLabel.text = dateFormatter.string(from: time.dateValue())
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let message = messages[indexPath.row].message
        return estimateFrameForText(text: message).height
    }
    
    private func estimateFrameForText(text: String) -> CGRect {
        let width = text.estimateWidth(usingFont: UIFont.systemFont(ofSize: 16))
        let height = text.estimateHeight(usingFont: UIFont.systemFont(ofSize: 16))
        if (height < 96) {
            return CGRect(x: 0, y: 0, width: width, height: 96)
        }
        return CGRect(x: 0, y: 0, width: 200, height: height)
    }
    @IBAction func tapBack(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    private func scrollBottom(animated: Bool){
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(300)) {
            let row = self.messages.count-1
            if  row > 0 {
                let indexPath = IndexPath(row: row, section: 0)
                self.tableView.scrollToRow(at: indexPath, at: .bottom, animated: animated)
            }
        }
    }
       
}

