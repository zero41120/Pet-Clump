//
//  FriendListCell.swift
//  PetClump
//
//  Created by Jerod Zheng on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class FriendListCell: UITableViewCell {
    @IBOutlet weak var animalImage: UIImageView!
    @IBOutlet weak var cellView: UIView!
    @IBOutlet weak var animalLbl: UILabel!
    @IBOutlet weak var animalTime: UILabel!
    @IBOutlet weak var animalChat: UILabel!
    
    @IBOutlet weak var acceptButton: UIButton!
    @IBOutlet weak var rejectButton: UIButton!
    
}
