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
    let iv: String
    init(refObject: [String: Any]){
        senderId = refObject["senderId"] as! String
        time = refObject["time"] as! Timestamp
        message = refObject["text"] as! String
        iv = refObject["iv"] as! String
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
    
    let myId: String
    let friendId: String
    let chatRoomId: String
    let roomRef: DocumentReference
    let chatRef: CollectionReference
    var queryDoc: QueryDocumentSnapshot?
    var listenerQuery: Query?
    
    init(myPet: PetProfile, friendPet: PetProfile) {
        self.myId = myPet.getId()
        self.friendId = friendPet.getId()
        self.chatRoomId = myId > friendId ? "\(myId)\(friendId)" : "\(friendId)\(myId)"
        self.roomRef = Firestore.firestore().collection("chats").document(self.chatRoomId)
        self.chatRef = roomRef.collection("message")
        
        self.roomRef.getDocument { (snap, error) in
            if let err = error { print(err) }
            if let doc = snap, !doc.exists {
                let place_holder = ["bigPrime" : "1",
                                    "priPrime" : "1",
                                    myPet.getId(): "public_key_place_holder",
                                    friendPet.getId(): "public_key_place_holder"]
                self.roomRef.setData(place_holder, completion: { (error) in
                    if let err = error { print(err) }
                })
            }
        }
    }

    func download(completion: @escaping (([Message]) -> Void)){
        // Download from firebase
        
        let query = queryDoc == nil ?
            chatRef.order(by: "time") :
            chatRef.order(by: "time").start(afterDocument: queryDoc!)

        query.getDocuments {(snap, err) in
            guard let snap = snap else {
                print("Error loding chats")
                return
            }
            var messages:[Message] = []
            
            guard let last = snap.documents.last else { return }
            if self.queryDoc == snap.documents.last { return }
            self.queryDoc = last
            
            print("last: \(last.data())")
            for doc in snap.documents {
                print("doc: \(doc.data())")
                messages.append(Message(refObject: doc.data()))
            }
            completion(messages)
        }
    }
    
    func upload(message: String, completion: @escaping ((Message) -> Void)){
        let msg = Message(refObject: ["senderId":myId, "time": Timestamp(), "text": message, "iv":"place_holder"])
        chatRef.document().setData(msg.generateDictionary()) { (error) in
            if let err = error {
                print(err)
                return
            }
            completion(msg)
        }
    }
}
