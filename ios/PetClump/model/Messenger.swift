//
//  Messenger.swift
//  PetClump
//
//  Created by YSH on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase


class Message{
    let senderId: String
    let time: Timestamp
    let message: String
    init(refObject: [String: Any]){
        senderId = refObject["senderId"] as! String
        time = refObject["time"] as! Timestamp
        message = refObject["message"] as! String
    }
}

class Messenger {
    
    let myId: String
    let friendId: String
    let chatRoomId: String
    var index: Int = -1
    init(myPet: PetProfile, friendPet: PetProfile) {
        self.myId = myPet.getId()
        self.friendId = friendPet.getId()
        self.chatRoomId = myId > friendId ? "\(myId)\(friendId)" : "\(friendId)\(myId)"
    }
    
    
    func download(count: Int, completion: (([Message]) -> Void)){
        // Download from firebase
        var messages: [Message] = [
            Message(refObject: ["senderId":"5Z2rd459CqXZFE3vrk7AQkYn1Yy10", "time": Timestamp(), "message": "Example Text 0"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 1"]),
            Message(refObject: ["senderId":"5Z2rd459CqXZFE3vrk7AQkYn1Yy10", "time": Timestamp(), "message": "Example Text 2"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 3"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 4"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 5"]),
            Message(refObject: ["senderId":"5Z2rd459CqXZFE3vrk7AQkYn1Yy10", "time": Timestamp(), "message": "Example Text 6"]),
            Message(refObject: ["senderId":"5Z2rd459CqXZFE3vrk7AQkYn1Yy10", "time": Timestamp(), "message": "Example Text 7"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 8"]),
            Message(refObject: ["senderId":"94OeeGargpPOI5RuQU9N9zb2qvD30", "time": Timestamp(), "message": "Example Text 9"]),
            Message(refObject: ["senderId":"5Z2rd459CqXZFE3vrk7AQkYn1Yy10", "time": Timestamp(), "message": "Example Text 10"])
        ]
        if index == -1 {
            index = messages.count
        }
        let subMessage = messages[..<index]
        var retMessage: [Message] = []
        var c = count
        for m in subMessage {
            retMessage.append(m)
            c = c - 1
            if c == 0 {
                break;
            }
        }
        completion(retMessage)
    }
}
