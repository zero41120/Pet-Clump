//
//  BestMatchingVC.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import FirebaseAuth

class BestMatchingVC: UIViewController{

    
    @IBOutlet weak var matchingTable: UITableView!

    var matchingDelegate: MatchingTableDelegate?
    // Assigned by caller view
    var petProfile: PetProfile?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard let _ = Auth.auth().currentUser?.uid else {
            dismiss(animated: true, completion: nil)
            return
        }

        petProfile!.download {
            print("\(self.petProfile!.generateDictionary())")
            self.matchingDelegate = MatchingTableDelegate(myPet: self.petProfile!, downloadLimit: 30, table: self.matchingTable, callerView: self)
            self.matchingTable.delegate = self.matchingDelegate
            self.matchingTable.dataSource = self.matchingDelegate
        }
    }

    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
