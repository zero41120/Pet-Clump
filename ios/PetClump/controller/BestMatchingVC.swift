//
//  BestMatchingVC.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth

class BestMatchingVC: UIViewController, ProfileDownloader{

    
    @IBOutlet weak var matchingTable: UITableView!

    var matchingDelegate: MatchingTableDelegate?
    // Assigned by caller view
    var petProfile: PetProfile?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let uid = Auth.auth().currentUser?.uid else {
            dismiss(animated: true, completion: nil)
            return
        }
        petProfile?.download(uid: uid, completion: self)
    }
    
    func didCompleteDownload() {
        matchingDelegate = MatchingTableDelegate(myPet: petProfile!, downloadLimit: 30, table: matchingTable)
        matchingTable.delegate = matchingDelegate
        matchingTable.dataSource = matchingDelegate
    }

    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
