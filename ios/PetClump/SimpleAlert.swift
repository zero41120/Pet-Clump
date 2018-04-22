//
//  SimpleAlert.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation

protocol QuickAlert {
    /**
     * This method make a alter with OK button.
     * - Parameter message: A string mess to show in the alter
     */
    func makeAlert(message: String)
}
