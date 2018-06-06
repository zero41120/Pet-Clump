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
    
    init(myPet: PetProfile, friendPet: PetProfile, completion: @escaping (Messenger) -> Void) {
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
                completion(self)
            }
        }
    }
    
    func getLastMessage(completion: @escaping (String, Timestamp)->Void){
        let listenerQuery = chatRef.order(by: "time", descending: true).limit(to: 1)
        listenerQuery.getDocuments { (snap, error) in
            if let err = error { print(err) }
            if let docs = snap?.documents {
                if let data = docs.first?.data() {
                    let msg = self.decodeData(data: data)
                    completion(msg.message, msg.time)
                } else {
                    completion(NSLocalizedString("Start messaging!", comment: "This is the place holder string when became friend without sending any message"), Timestamp())
                }
            }
        }
    }
    
    func startListen(handler: @escaping (([Message]) -> Void)){
        let listenerQuery = chatRef.order(by: "time")
        listenerQuery.addSnapshotListener { (snap, error) in
            guard error == nil else { return }
            guard let snap = snap else { return }
            var messages: [Message] = []
            for doc in snap.documents {
                messages.append(self.decodeData(data: doc.data()))
            }
            handler(messages)
        }
    }
    
    func decodeData(data: [String : Any])->Message {
        print("Listened: \(data)")
        let cG = Cryptographer.getInstance()
        let msg = Message(refObject: data)
        let iv: [UInt8] = cG.convertIV(iv: msg.iv)
        let plainText = cG.decrypt(key: self.key, iv: iv, cipherText: msg.message)
        msg.message = plainText
        return msg
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
