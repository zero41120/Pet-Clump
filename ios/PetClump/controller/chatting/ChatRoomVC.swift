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
    
    // Assign by caller
    var friendPetProfile: PetProfile?
    var myPetProfile: PetProfile?
    var messages: [Message] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.register(MessageCell.self, forCellReuseIdentifier: "cell")
        tableView.separatorColor = UIColor.clear
        tableView.delegate = self
        tableView.dataSource = self
        
        // Download Message
        let messenger = Messenger(myPet: myPetProfile!, friendPet: friendPetProfile!)
        messenger.download(count: 5) { (retMessages) in
            messages = retMessages
            tableView.reloadData()
        }
    }
    
    @objc func handleSend() {
        // Connect firebase
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        handleSend()
        return true
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let time = messages[indexPath.row].time
        let message = messages[indexPath.row].message
        let senderId = messages[indexPath.row].senderId
        let senderCellId = senderId == myPetProfile?.getId() ? MessageCell.myCellId : MessageCell.friendCellId
        let senderImageUrl =
            senderId == myPetProfile?.getId() ?
            myPetProfile?.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main) :
            friendPetProfile?.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main)
        let cell = tableView.dequeueReusableCell(withIdentifier: senderCellId) as! MessageCell
        
        // Text field
        cell.textField.text = message
        cell.textField.frame = estimateFrameForText(text: message)
        cell.textField.topAnchor.constraint(equalTo: cell.containerView.topAnchor, constant: 8)
        cell.textField.rightAnchor.constraint(equalTo: cell.petImage.rightAnchor, constant: 8)
        cell.textField.contentInset = UIEdgeInsetsMake(1, 8, 1, 8);
        cell.makeBubble(backColor: UIColor(red: 0, green: 137/255, blue: 249/255, alpha: 1), textColor: UIColor.white)
        
        // Image
        cell.petImage.load(url: senderImageUrl!)
        
        // Time
        let hour = Calendar.current.component(.hour, from: time.dateValue())
        let minute = Calendar.current.component(.minute, from: time.dateValue())
        print("\(hour)\(minute)")
        cell.timeLabel.text = "\(hour)\(minute)"
        
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
}

