//
//  MatchingTableDelegate.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class MatchingTableDelegate: NSObject, UITableViewDataSource, UITableViewDelegate {
    var elementLeft = [1,2,3,4,5]
    var elementRight = [6,7,8,9,10]

    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return elementLeft.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MatchingCell") as! MatchingCell
        
        cell.labelLeft.text = "\(elementLeft[indexPath.row])"
        cell.labelRight.text = "\(elementRight[indexPath.row])"
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 200
    }
    
    
}
