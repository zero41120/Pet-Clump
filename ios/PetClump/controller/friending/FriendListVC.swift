//
//  FriendListVC.swift
//  PetClump
//
//  Created by Jerod Zheng on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class FriendListVC: GeneralVC, UITableViewDelegate, UITableViewDataSource {

    var myPet: PetProfile = PetProfile.most_recent_pet!
    
    //this is the array for each fof your riend's name
    var friendHandlers: [FriendHandler] = []
    var messages: [String] = []
    var times: [String] = []
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        tableView.delegate = self
        tableView.dataSource = self
        self.friendHandlers = []
        FriendHandler.downloadFriends(myProfile: PetProfile.most_recent_pet!, callerView: self) { (fHandler) in
            fHandler.isFriending(ifTrue: {
                self.friendHandlers.append(fHandler)
                self.tableView.reloadData()
            }, ifFalse: nil)
            fHandler.isReceiving(ifTrue: {
                self.friendHandlers.append(fHandler)
                self.tableView.reloadData()
            }, ifFalse: nil)
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let friendHandler = friendHandlers[indexPath.row]
        friendHandler.isFriending(ifTrue: {
            // Start messaging for friends
            let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
            let pdv = storyBoard.instantiateViewController(withIdentifier: "ChatRoomVC") as! ChatRoomVC
            pdv.myPetProfile = friendHandler.myPet
            pdv.friendPetProfile = friendHandler.friendPet
            self.present(pdv, animated: true, completion: nil)
        }, ifFalse: {
            // View the profile of the friend request sender
            self.tableView.deselectRow(at: indexPath, animated: true)
            let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
            let pdv = storyBoard.instantiateViewController(withIdentifier: "MatchingViewVC") as! MatchDetailVC
            MatchTabBar.thisPet = self.friendHandlers[indexPath.row].myPet
            MatchTabBar.thatPet = self.friendHandlers[indexPath.row].friendPet
            self.present(pdv, animated: true, completion: nil)
        })
        
    }

    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return friendHandlers.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let cell = tableView.dequeueReusableCell(withIdentifier: "customCell") as! FriendListCell
        let friendHandler = friendHandlers[indexPath.row]
        let thatPet = friendHandler.friendPet
        let thisPet = friendHandler.myPet
        
        friendHandler.isFriending(
            ifTrue: {
                cell.acceptButton.isHidden = true
                cell.rejectButton.isHidden = true
                let _ = Messenger(myPet: thisPet, friendPet: thatPet, completion: { (messenger) in
                    messenger.getLastMessage(completion: { (text, time) in
                        cell.animalChat.text = text
                        cell.animalTime.text = time.getHourMinute()
                    })
                })
            }, ifFalse: {
                cell.animalChat.text = NSLocalizedString("Added you!", comment: "This is message show to you when another user send you a friend request")
                cell.animalTime.text = ""
                cell.acceptButton.tag = indexPath.row
                cell.rejectButton.tag = indexPath.row
                cell.acceptButton.addTarget(self, action: #selector(self.acceptFriend), for: .touchUpInside)
                cell.rejectButton.addTarget(self, action: #selector(self.rejectFriend), for: .touchUpInside)
            }
        )
        cell.animalImage.load(url: thatPet.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
        cell.animalImage.setRounded()
        cell.animalImage.tag = indexPath.row
        let tap = UITapGestureRecognizer(target: self, action: #selector(self.viewProfileOfFriend(sender:)))
        cell.animalImage.isUserInteractionEnabled = true
        cell.animalImage.addGestureRecognizer(tap)
        cell.animalLbl.text = thatPet.name
        return cell
    }
    
    @objc func viewProfileOfFriend(sender: UITapGestureRecognizer){
        let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "MatchTabBar") as! MatchTabBar
        MatchTabBar.thisPet = friendHandlers[sender.view!.tag].myPet
        MatchTabBar.thatPet = friendHandlers[sender.view!.tag].friendPet
        self.present(pdv, animated: true, completion: nil)
    }
    
    @objc func acceptFriend(sender: UIButton){
        let row = sender.tag
        let fHandler = self.friendHandlers[row]
        fHandler.acceptFriend()
        self.tableView.reloadData()
    }
    
    @objc func rejectFriend(sender: UIButton){
        let row = sender.tag
        let fHandler = self.friendHandlers[row]
        confirmBefore(doing: {
            fHandler.rejectFriend()
        }, title: NSLocalizedString("Reject", comment: "This is title of an alert when user select reject to respond a friend request"),
           message: NSLocalizedString("Are you sure you want to reject this request?", comment: "This is message of an alert when user select reject to respond a friend request"))
        self.tableView.reloadData()
    }
    
    
    // Dismisses the view
    @IBAction func tapCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}






