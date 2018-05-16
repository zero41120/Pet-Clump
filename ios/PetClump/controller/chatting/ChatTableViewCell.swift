//
//  ChatTableViewCell.swift
//  PetClump
//
//  Created by admin on 5/13/18.
//  Copyright © 2018 PetClump. All rights reserved.
//


import UIKit

class ChatTableViewCell: UITableViewCell {
    
    @IBOutlet weak var lblText: UILabel!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
}
