//
//  MatchingCell.swift
//  PetClump
//
//  Created by YSH on 5/9/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class MatchingTableViewCell: UITableViewCell {
    @IBOutlet weak var imageRight: UIImageView!
    @IBOutlet weak var imageLeft: UIImageView!
    @IBOutlet weak var labelRightMatch: UILabel!
    @IBOutlet weak var labelLeftMatch: UILabel!
    @IBOutlet weak var labelRightLoc: UILabel!
    @IBOutlet weak var labelLeftLoc: UILabel!
    
    func setupLable(){
        self.selectionStyle = UITableViewCellSelectionStyle.none
        self.labelLeftLoc.textColor = StyleProvider.white
        self.labelLeftLoc.font = StyleProvider.getDefaultFont(12)
        self.labelLeftLoc.backgroundColor = StyleProvider.primaryColor
        self.labelLeftMatch.textColor = StyleProvider.white
        self.labelLeftMatch.backgroundColor = StyleProvider.darkGreen
        self.labelLeftMatch.font = StyleProvider.getDefaultFont(12)
        
        self.labelRightLoc.textColor = StyleProvider.white
        self.labelRightLoc.backgroundColor = StyleProvider.primaryColor
        self.labelRightLoc.font = StyleProvider.getDefaultFont(12)
        self.labelRightMatch.textColor = StyleProvider.white
        self.labelRightMatch.backgroundColor = StyleProvider.darkGreen
        self.labelRightMatch.font = StyleProvider.getDefaultFont(12)
    }
}
