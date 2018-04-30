//
//  LimitTextViewInput.swift
//  PetClump
//
//  Created by YSH on 4/29/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class LimitTextViewInput: NSObject, UITextViewDelegate {
    private let limitCount: Int
    private let remainingLable: UILabel

    
    init(count: Int, remainingLable: UILabel){
        self.limitCount = count
        self.remainingLable = remainingLable
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        guard let t = textView.text else { return true }
        let newLength = t.count + text.count - range.length
        print("\(newLength)/\(limitCount)")
        self.remainingLable.text = "\(newLength)/\(limitCount)"
        return newLength <= self.limitCount // Bool
    }
}

