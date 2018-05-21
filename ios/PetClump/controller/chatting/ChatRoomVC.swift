//
//  ChatRoomVC.swift
//  PetClump
//
//  Created by YSH on 5/18/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class ChatRoomVC: UIViewController, UITextFieldDelegate {
    
    
    @IBOutlet weak var navBar: UINavigationBar!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var sendButton: UIButton!
    @IBOutlet weak var inputField: UITextField!
    
    var messageDelegate: PetMessageTableDelegate?
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        messageDelegate = PetMessageTableDelegate()
        tableView.register(MessageCell.self, forCellReuseIdentifier: "cell")
        tableView.separatorColor = UIColor.clear
        tableView.delegate = messageDelegate
        tableView.dataSource = messageDelegate
    }
    
    @objc func handleSend() {
        // Connect firebase
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        handleSend()
        return true
    }
}

class PetMessageTableDelegate: NSObject, UITableViewDelegate, UITableViewDataSource {
    var messages: [Message] = [
        Message(refObject: ["":""]),Message(refObject: ["":""]),Message(refObject: ["":""])
    ]
    
    var width = CGFloat(200)
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell: MessageCell = tableView.dequeueReusableCell(withIdentifier: MessageCell.cellId) as! MessageCell
        
        let message = messages[indexPath.row].message
        cell.textField.text = message
        width = cell.textField.frame.width
        cell.textField.frame = estimateFrameForText(text: message, width: width)
        cell.textField.topAnchor.constraint(equalTo: cell.containerView.topAnchor, constant: 8)
        cell.textField.rightAnchor.constraint(equalTo: cell.petImage.rightAnchor, constant: 8)
        cell.makeBubble(backColor: UIColor(red: 0, green: 137/255, blue: 249/255, alpha: 1), textColor: UIColor.white)
        cell.textField.contentInset = UIEdgeInsetsMake(1, 1, 1, 1);

        cell.petImage.backgroundColor = UIColor.red
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let message = messages[indexPath.row].message
        return estimateFrameForText(text: message, width: self.width).height
    }
    
    private func estimateFrameForText(text: String, width: CGFloat) -> CGRect {
        let size = CGSize(width: width, height: 1000)
        let options = NSStringDrawingOptions.usesFontLeading.union(.usesLineFragmentOrigin)
        return NSString(string: text).boundingRect(with: size, options: options, attributes: [NSAttributedStringKey.font: UIFont.systemFont(ofSize: 18)], context: nil)
    }
}

class Message{
    let senderId: String
    let time: Timestamp
    let message: String
    init(refObject: [String: Any]){
        senderId = "No id"
        time = Timestamp()
        message = "哈囉你好嗎？衷心感謝，珍重再見，期待再相逢！哈囉你好嗎？衷心感謝，珍重再見，期待再相逢！"
    }
}
