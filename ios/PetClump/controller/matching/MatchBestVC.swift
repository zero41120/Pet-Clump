//
//  BestMatchingVC.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class MatchBestVC: UIViewController, UITableViewDataSource, UITableViewDelegate{

    @IBOutlet weak var matchingTable: UITableView!
    
    let db = Firestore.firestore()
    var query: Query?
    var element: [MatchingProfile] = []
    var downloadLimit: Int = 30
    
    // Assigned by caller view
    var petProfile: PetProfile? = PetProfile.most_recent_pet
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            dismiss(animated: true, completion: nil)
            return
        }

        petProfile!.download {
            self.matchingTable.delegate = self
            self.matchingTable.dataSource = self
            self.query = self.db.collection("pets").limit(to: self.downloadLimit)
            self.downloadMore()
        }
    }

    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
    
    func downloadMore(){
        guard let uid = Auth.auth().currentUser?.uid else { return }
        query?.addSnapshotListener{(snap, err) in
            if let error = err {
                print("Error loding matches: \(error)")
                return
            }
            guard let snap = snap else {
                print("Error loding matches")
                return
            }
            
            guard let last = snap.documents.last else { return }
            var downloadSize = snap.documents.count
            let quizResult = self.petProfile!.quiz
            var toSort: [MatchingProfile] = [] {
                didSet {
                    if downloadSize == toSort.count {
                        toSort.sort(by: MatchingProfile.matcherSorter)
                        self.element.append(contentsOf: toSort)
                        self.query = self.db.collection("pets").start(afterDocument: last).limit(to: self.downloadLimit)
                        self.matchingTable.reloadData()
                    }
                }
            }
            for doc in snap.documents{
                // Set up variable
                let thisPet = PetProfile.most_recent_pet!
                let thatPet = PetProfile(refObj: doc.data())
                
                // Skip your own pet
                if thatPet.ownerId == uid {
                    downloadSize -= 1
                    continue
                }
                
                // Skip your friend
                let friendHandler = FriendHandler(myProfile: thisPet, friendProfile: thatPet)
                friendHandler.isFriending(
                    ifTrue: {
                        downloadSize -= 1
                    }, ifFalse: {
                    // Downloads that owner and process matching %
                    let _ = OwnerProfile(id: thatPet.ownerId, completion: { (owner) in
                        let match = MatchingProfile(thatOwner: owner, thatPet: thatPet)
                        print("\(thatPet.name): q:\(match.quiz) l:\(match.location)")
                        toSort.append(match)
                    })
                })
            }
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Int(ceil(Double(element.count) / 2))
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        // Setups for cell
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchingCell") as! MatchingTableViewCell
        cell.selectionStyle = UITableViewCellSelectionStyle.none
        let tapl = UITapGestureRecognizer(target: self, action: #selector(viewMatching(sender:)))
        let tapr = UITapGestureRecognizer(target: self, action: #selector(viewMatching(sender:)))
        let index = indexPath.row * 2
        
        // Set required cell
        let left = element[index]
        cell.imageLeft.tag = index
        cell.labelLeft.text = "\(left.getMatchPercent())\t\(left.getDistance())"
        cell.imageLeft.load(url: left.getPhotoUrl())
        cell.imageLeft.isUserInteractionEnabled = true
        cell.imageLeft.addGestureRecognizer(tapl)
        
        // Finish setup if optional is empty
        if !element.indices.contains(index + 1) {
            cell.labelRight.layer.isHidden = true
            cell.imageRight.layer.isHidden = true
            return cell
        }
        
        // Set optional cell
        let right =  element[index + 1]
        cell.imageRight.tag = index + 1
        cell.labelRight.text = "\(right.getMatchPercent())\t\(right.getDistance())"
        cell.imageRight.load(url: right.getPhotoUrl())
        cell.imageRight.isUserInteractionEnabled = true
        cell.imageRight.addGestureRecognizer(tapr)
        cell.labelRight.layer.isHidden = false
        cell.imageRight.layer.isHidden = false
        return cell
    }
    
    @objc func viewMatching(sender: UITapGestureRecognizer){
        let storyBoard: UIStoryboard = UIStoryboard(name: "Message", bundle: nil)
        let pdv = storyBoard.instantiateViewController(withIdentifier: "MatchDetailVC") as! MatchDetailVC
        let index = sender.view!.tag
        MatchTabBar.thatPet = element[index].getProfile()
        MatchTabBar.thisPet = petProfile
        self.present(pdv, animated: true, completion: nil)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 200
    }
    
    func tableView(_ tableView: UITableView, willDisplay cell: UITableViewCell, forRowAt indexPath: IndexPath) {
        let last = element.count / 2
        if indexPath.row == last {
            self.downloadMore()
        }
    }
}
