//
//  BestMatchingVC.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class BestMatchingVC: UIViewController{

    
    @IBOutlet weak var matchingTable: UITableView!

    var matchingDelegate: MatchingTableDelegate?
    // Assigned by caller view
    var petProfile: PetProfile?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        matchingDelegate = MatchingTableDelegate()
        matchingTable.delegate = matchingDelegate
        matchingTable.dataSource = matchingDelegate
        
    }

}
