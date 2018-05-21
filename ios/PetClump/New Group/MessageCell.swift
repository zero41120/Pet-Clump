//
//  MessageCell.swift
//  PetClump
//
//  Created by YSH on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class MessageCell: UITableViewCell{

    @IBOutlet weak var containerView: UIView!
    @IBOutlet weak var textField: UITextView!
    @IBOutlet weak var petImage: UIImageView!
    static let cellId = "MessageCell"
  
    func makeBubble(backColor: UIColor, textColor: UIColor){
        textField.layer.cornerRadius = 16
        textField.backgroundColor = backColor
        textField.textColor = textColor
    }
}
