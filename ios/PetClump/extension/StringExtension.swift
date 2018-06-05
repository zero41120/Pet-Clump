//
//  StringExtension.swift
//  PetClump
//
//  Created by YSH on 5/16/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit


extension String{
    
    func urlEscape() -> String{
        return self.addingPercentEncoding(withAllowedCharacters: .urlHostAllowed)!
    }
    
    func estimateWidth(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.width
    }
    
    func estimateHeight(usingFont font: UIFont) -> CGFloat {
        let fontAttributes = [NSAttributedStringKey.font: font]
        let size = self.size(withAttributes: fontAttributes)
        return size.height
    }
    
    func getCGSize(usingFont font: UIFont) -> CGSize {
        let fontAttributes = [NSAttributedStringKey.font: font]
        return self.size(withAttributes: fontAttributes)
    }
    
    func contains(toSearch: String) -> Bool {
        return self.range(of: toSearch) != nil
    }
    
    /**
     https://stackoverflow.com/questions/27067508
     String extension that extract the captured groups with a regex pattern
     
     - parameter pattern: regex pattern
     - Returns: captured groups
     */
    public func capturedGroups(withRegex pattern: String) -> [String] {
        var results = [String]()
        
        var regex: NSRegularExpression
        do {
            regex = try NSRegularExpression(pattern: pattern, options: [])
        } catch {
            return results
        }
        let matches = regex.matches(in: self, options: [], range: NSRange(location:0, length: self.count))
        
        guard let match = matches.first else { return results }
        
        let lastRangeIndex = match.numberOfRanges - 1
        guard lastRangeIndex >= 1 else { return results }
        
        for i in 1...lastRangeIndex {
            let capturedGroupIndex = match.range(at: i)
            let matchedString = (self as NSString).substring(with: capturedGroupIndex)
            results.append(matchedString)
        }
        
        return results
    }
}
