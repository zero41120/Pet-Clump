//
//  Messenger.swift
//  PetClump
//
//  Created by YSH on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase
import BigInt


class Message{
    let senderId: String
    let time: Timestamp
    var message: String
    let iv: String
    init(refObject: [String: Any]){
        self.senderId = refObject["senderId"] as! String
        self.time = refObject["time"] as! Timestamp
        self.message = refObject["text"] as? String ?? "Error Message"
        self.iv = refObject["iv"] as? String ?? "No iv"
        print("message: \(self.message)")
    }
    func generateDictionary() -> [String: Any]{
        return ["senderId":senderId, "time":time, "text":message, "iv":iv]
    }
    
    func equals(obj: Message) -> Bool{
        return  self.senderId == obj.senderId &&
                self.time == obj.time &&
                self.message == obj.message &&
                self.iv == obj.iv
    }
}

class Messenger {
    var key: Array<UInt8> = []
    let myId: String
    let chatRoomId: String
    let chatRef: CollectionReference
    
    init(myPet: PetProfile, friendPet: PetProfile, completion: @escaping () -> Void) {
        self.chatRoomId = myPet.generateChatRoomId(otherProfile: friendPet)
        self.myId = myPet.getId()
        let roomRef = Firestore.firestore().collection("chats").document(self.chatRoomId)
        self.chatRef = roomRef.collection("message")
        roomRef.getDocument { (snap, error) in
            if let err = error { print(err) }
            if let doc = snap, doc.exists {
                let data = doc.data()!
                let friendId = friendPet.getId()
                let friendPublic = BigInt(data[friendId] as! String)!
                let dh = KeyExchanger(acceptFriendId: friendId, data: data)
                self.key = dh.getSharedKey(fdPublic: friendPublic)
                completion()
            }
        }
    }
    
    func startListen(handler: @escaping (([Message]) -> Void)){
        let listenerQuery = chatRef.order(by: "time")
        let cG = Cryptographer.getInstance()
        listenerQuery.addSnapshotListener { (snap, error) in
            guard error == nil else { return }
            guard let snap = snap else { return }
            var messages: [Message] = []
            for doc in snap.documents {
                let data = doc.data();
                print("Listened: \(data)")
                let msg = Message(refObject: data)
                let iv: [UInt8] = cG.convertIV(iv: msg.iv)
                let plainText = cG.decrypt(key: self.key, iv: iv, cipherText: msg.message)
                msg.message = plainText
                messages.append(msg)
            }
            handler(messages)
        }
    }

    func upload(message: String, completion: @escaping ((Message) -> Void)){
        let cG = Cryptographer.getInstance()
        let iv = cG.generateInitializationVector()
        let ivString = "\(iv)"
        let cipher = cG.encrypt(key: self.key, iv: iv, plainText: message)
        let msg = Message(refObject: ["senderId":myId, "time": Timestamp(), "text": cipher, "iv":ivString])
        chatRef.document().setData(msg.generateDictionary()) { (error) in
            if let err = error {
                print(err)
                return
            }
            completion(msg)
        }
    }
}
