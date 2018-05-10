//
//  MatchingTableDelegate.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class MatchingTableDelegate: NSObject, UITableViewDataSource, UITableViewDelegate {

    var element: [MatchingProfile] = []
    
    var downloadLimit: Int
    var profile: PetProfile

    let db = Firestore.firestore()
    var query: Query
    
    let table: UITableView
    

    
    init(myPet: PetProfile, downloadLimit: Int, table: UITableView){
        self.profile = myPet
        self.downloadLimit = downloadLimit
        self.table = table
        query = db.collection("pets").limit(to: self.downloadLimit)
        super.init()
        self.downloadMore()
    }
    
    func downloadMore(){
        query.addSnapshotListener{(snap, err) in
            guard let snap = snap else {
                print("Error loding matches")
                return
            }
        
            guard let last = snap.documents.last else { return }
            let quizResult = self.profile.quiz
            var toSort: [MatchingProfile] = []
            for doc in snap.documents{
                let petProfile = PetProfile(refObj: doc.data())
                let newMatchProfile = MatchingProfile(quizResult: quizResult, petProfile: petProfile)
                toSort.append(newMatchProfile)
            }
            toSort.sort(by: MatchingProfile.matcherSorter)
            self.element.append(contentsOf: toSort)
            self.query = self.db.collection("pets").start(afterDocument: last).limit(to: self.downloadLimit)
            self.table.reloadData()
        }
    }

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return Int(ceil(Double(element.count) / 2))
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchingCell") as! MatchingCell
        cell.selectionStyle = UITableViewCellSelectionStyle.none
        hideCell(cell: cell)
        let index = indexPath.row * 2
        let left = element[index]
        cell.labelLeft.text = left.getMatchPercent()
        cell.imageLeft.load(url: left.getPhotoUrl())
        if element.indices.contains(index + 1) {
            cell.labelRight.layer.isHidden = false
            cell.imageRight.layer.isHidden = false
            let right =  element[index + 1]
            cell.labelRight.text = right.getMatchPercent()
            cell.imageRight.load(url: right.getPhotoUrl())
        }
        
        return cell
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
    
    func hideCell(cell: MatchingCell){
        cell.labelRight.layer.isHidden = true
        cell.imageRight.layer.isHidden = true
    }
}
