//
//  Friend.swift
//  PetClump
//
//  Created by YSH on 5/28/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

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
    let motherView: UIViewController
    
    var myRecord: DocumentReference
    var theirRecord: DocumentReference
    
    private var myPending: String?
    private var friendPending: String?
    
    init(myProfile: PetProfile, friendProfile: PetProfile, caller: UIViewController){
        self.myPet = myProfile
        self.friendPet = friendProfile
        self.motherView = caller
        
        self.myRecord = Firestore.firestore().collection("pets").document(myPet.getId()).collection("friends").document(friendPet.getId())
        self.theirRecord = Firestore.firestore().collection("pets").document(friendPet.getId()).collection("friends").document(myPet.getId())
        
    }
    
    private func getMyPending(completion: @escaping (()->Void)){
        
        if myPending != nil {
            print("myPending exists: \(myPending!)")
            completion()
        }
        myRecord.getDocument(completion: { (snap , error) in
            if let document = snap, document.exists {
                self.myPending = document.get("pending") as? String
                print("downloaded myPending: \(self.myPending!)")
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
    
    func isBlocking(ifTrue: (() -> Void)?, ifFalse: (() -> Void)?){
        getMyPending {
            switch self.myPending {
            case PendingStatus.BLOCKING: ifTrue?()
            default: ifFalse?()
            }
        }
    }
    
    func addFriend(completion: ()->Void){
        // TODO put DH public variable here
        let senderData = ["pending" : PendingStatus.SENDING]
        let receiverData  = ["pending" : PendingStatus.RECEIVING]
        
        myRecord.setData(senderData) { (error) in
            if let err = error {
                print(err)
            }
        }
    
        theirRecord.setData(receiverData) { (error) in
            if let err = error {
                print(err)
            }
        }
        // Asyn is probably okay here
        completion()
    }
    
    func acceptFriend(){
        let data = ["pending" : PendingStatus.FRIENDING]
        myRecord.setData(data) { (error) in
            if let err = error {
                print(err)
            }
        }
        theirRecord.setData(data) { (error) in
            if let err = error {
                print(err)
            }
        }
    }

    func rejectFriend(){
        self.myRecord.delete(){ err in
            if let err = err {
                print("Error removing document: \(err)")
            } else {
                print("Document successfully removed!")
            }
        }
        self.theirRecord.delete(){ err in
            if let err = err {
                print("Error removing document: \(err)")
            } else {
                print("Document successfully removed!")
            }
        }
    }
    
    static func downloadFriends(myProfile: PetProfile, callerView: UIViewController, downloaded: @escaping (FriendHandler) -> Void){
        var _ = Firestore.firestore().collection("pets").document(myProfile.getId()).collection("friends").getDocuments { (snap, error) in
            if let err = error {
                print("Error getting documents: \(err)")
            } else {
                for document in snap!.documents {
                    let _ = PetProfile(id: document.documentID, completion: { (friendPet) in
                        downloaded(FriendHandler(myProfile: myProfile, friendProfile: friendPet, caller: callerView))
                        print("downloaded: \(friendPet.getId())")
                    })
                }
            }
        }
    }
    
}
