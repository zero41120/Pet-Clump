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
        setupKeyboardObservers()
        tableView.register(MessageCell.self, forCellReuseIdentifier: "cell")
        tableView.separatorColor = UIColor.clear
        tableView.delegate = self
        tableView.dataSource = self
        tableView.keyboardDismissMode = .onDrag

        sendButton.addTarget(self, action: #selector(handleSend), for: .touchUpInside)
        inputField.delegate = self
        
        // TODO Download Message
        messenger = Messenger(myPet: myPetProfile!, friendPet: friendPetProfile!)
        messenger!.download(count: 5) { (retMessages) in
            messages = retMessages
            tableView.reloadData()
        }
    }
    
    
    func setupKeyboardObservers(){
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        if let keyboardSize = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            self.bottomConstraint.constant = -(keyboardSize.height + 88)
            self.view.layoutIfNeeded()
            self.scrollBottom()
        }
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        if let _ = (notification.userInfo?[UIKeyboardFrameBeginUserInfoKey] as? NSValue)?.cgRectValue {
            bottomConstraint.constant = 0
            self.view.layoutIfNeeded()
            self.scrollBottom()
        }
    }
    
    @objc func handleSend() {
        guard self.inputField!.text != ""  else { return }
        messenger?.upload(message: self.inputField!.text!, completion: { (message) in
            self.inputField!.text = ""
            messages = messages + message
            tableView.reloadData()
            self.scrollBottom()
        })
        
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let last = messages.count
        if indexPath.row == last - 1 {
            messenger!.download(count: 10) { (retMessages) in
                if retMessages.count < 1 {
                    return
                }
                messages = messages + retMessages
                tableView.reloadData()
                self.scrollBottom()
            }
        }
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
    
    private func scrollBottom(){
        DispatchQueue.main.asyncAfter(deadline: .now() + .milliseconds(300)) {
            let indexPath = IndexPath(row: self.messages.count-1, section: 0)
            print("\(self.messages.count-1)")
            self.tableView.scrollToRow(at: indexPath, at: .bottom, animated: true)
        }
    }
       
}

