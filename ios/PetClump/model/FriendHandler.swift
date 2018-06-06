//
//  Friend.swift
//  PetClump
//
//  Created by YSH on 5/28/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase
import BigInt

class FriendHandler {
    
    class PendingStatus {
        static let SENDING = "sending"
        static let RECEIVING = "receiving"
        static let BLOCKING = "blocking"
        static let FRIENDING = "friending"
        private init() { }
    }
    
    let myPet: PetProfile
    let friendPet: PetProfile
    
    var myRecord: DocumentReference
    var theirRecord: DocumentReference
    var chatRoomRef: DocumentReference
    
    private var myPending: String?
    private var friendPending: String?
    
    init(myProfile: PetProfile, friendProfile: PetProfile){
        self.myPet = myProfile
        self.friendPet = friendProfile
        
        self.myRecord = Firestore.firestore().collection("pets").document(myPet.getId()).collection("friends").document(friendPet.getId())
        self.theirRecord = Firestore.firestore().collection("pets").document(friendPet.getId()).collection("friends").document(myPet.getId())
        
        let chatRoomId = myPet.generateChatRoomId(otherProfile: friendPet)
        self.chatRoomRef = Firestore.firestore().collection("chats").document(chatRoomId)
    }
    
    private func getMyPending(completion: @escaping (()->Void)){
        
        if myPending != nil {
            print("myPending exists: \(myPending!)")
            completion()
            return
        }
        myRecord.getDocument(completion: { (snap , error) in
            if let document = snap, document.exists {
                self.myPending = document.get("pending") as? String ?? ""
                completion()
            } else {
                self.myPending = ""
                completion()
            }
        })
    }
    
    func isPending(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.SENDING, PendingStatus.RECEIVING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func isSending(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.SENDING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func isReceiving(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.RECEIVING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func isFriending(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.FRIENDING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func shouldDisableAddFriendButton(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case "": ifFalse?()
            default: ifTrue?()
            }
        }
    }
    
    func isBlocking(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.BLOCKING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func addFriend(completion: ()->Void){
        let senderData = ["pending" : PendingStatus.SENDING]
        let receiverData  = ["pending" : PendingStatus.RECEIVING]
        let myDH = KeyExchanger(friendId: self.friendPet.getId())
        let publicData =
            myDH.generateSenderData(myPetId: self.myPet.getId(),
                                    friendPetId: self.friendPet.getId())
        self.chatRoomRef.setData(publicData, completion: { (error) in
            if let err = error { print(err) }
        })
        self.myRecord.setData(senderData, completion: nil)
        self.theirRecord.setData(receiverData, completion: nil)
        // Asyn is probably okay here
        completion()
    }
    
    func acceptFriend(completion: @escaping ()->Void){
        // Public vairable is generated in the chat room when user tap add friend
        self.chatRoomRef.getDocument { (snap, error) in
            if let err = error { print(err) }
            if let doc = snap, doc.exists {
                var data = doc.data()!
                let fId = self.friendPet.getId()
                let mId = self.myPet.getId()
                let myDH = KeyExchanger(acceptFriendId: fId, data: data)
                data[mId] = "\(myDH.getMyPublic())"
                self.chatRoomRef.setData(data) { _ in
                    completion()
                }
            }
        }
        
        let data = ["pending" : PendingStatus.FRIENDING]
        myRecord.setData(data)
        theirRecord.setData(data)
    }

    func rejectFriend(){
        self.chatRoomRef.delete()
        self.myRecord.delete()
        self.theirRecord.delete()
    }
    
    static func downloadFriends(myProfile: PetProfile, callerView: UIViewController, downloaded: @escaping (FriendHandler) -> Void){
        var _ = Firestore.firestore().collection("pets").document(myProfile.getId()).collection("friends").getDocuments { (snap, error) in
            if let err = error {
                print("Error getting documents: \(err)")
            } else {
                for document in snap!.documents {
                    let _ = PetProfile(id: document.documentID, completion: { (friendPet) in
                        downloaded(FriendHandler(myProfile: myProfile, friendProfile: friendPet))
                        print("downloaded: \(friendPet.getId())")
                    })
                }
            }
        }
    }
    
}
