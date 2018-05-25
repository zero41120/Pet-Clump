//
//  FriendListVC.swift
//  PetClump
//
//  Created by Jerod Zheng on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class FriendListVC: UIViewController, UITableViewDelegate, UITableViewDataSource {

    // Assign by caller view
    var myPet: PetProfile = PetProfile.most_recent_pet!
    
    //this is the array for each fof your riend's name
    let elements = ["horse", "cat", "dog", "potato","horse", "cat", "dog", "potato","horse", "cat", "dog", "potato"]
    
    // this is the array for each of the friend's chat history
    let message = ["Hello", "Hi", "How are you", "Nice to meet you","Hello", "Hi", "How are you", "Nice to meet you","Hello", "Hi", "How are you", "Nice to meet you" ]
    
    // this is the array for each of the friend's time last message sent.
    let time = ["12:40", "15:35", "08:55", "23:11", "12:40", "15:35", "08:55", "23:11", "12:40", "15:35", "08:55", "23:11"]
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        tableView.delegate = self
        tableView.dataSource = self
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "ChatRoomVC") as! ChatRoomVC
        pdv.myPetProfile = myPet
        self.present(pdv, animated: true, completion: nil)
    }
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return elements.count
    }
    
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        //load each cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "customCell") as! FriendListCell
        
        //loading the name of each frineds
        cell.animalLbl.text = elements[indexPath.row]
        
        //loading the image of each friend
        cell.animalImage.image = UIImage(named: elements[indexPath.row])
        //make the image round
        cell.animalImage.layer.cornerRadius = cell.animalImage.frame.height / 2
        
        //load the message of each friend
        cell.animalChat.text = message[indexPath.row]
        //load the time of each friend chat
        cell.animalTime.text = time[indexPath.row]
        
        return cell
    }
    
    // Dismisses the view
    @IBAction func tapCancel(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}






