//
//  FriendListTableViewCell.swift
//  PetClump
//
//  Created by Jerod Zheng on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class FriendListTableViewCell: UITableViewCell {


    @IBOutlet weak var animalImage: UIImageView!
    @IBOutlet weak var cellView: UIView!
    @IBOutlet weak var animalLbl: UILabel!
    @IBOutlet weak var animalTime: UILabel!
    @IBOutlet weak var animalChat: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

}
