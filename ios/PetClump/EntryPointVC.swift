//
//  ViewController.swift
//  PetClump
//
//  Created by YSH on 4/11/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase
import GoogleSignIn
import FacebookLogin


class EntryPointVC: UIViewController, GIDSignInUIDelegate {


    @IBOutlet weak var signInButton: GIDSignInButton!
    @IBOutlet weak var signOutButton: UIButton!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().signIn()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    /**
     * This action signs out the user if signed in, then alerts the result.
     * - Parameter sender: the action sender
     */
    @IBAction func topSignOut(_ sender: Any) {
        // Gets the authentication instance
        let firebaseAuth = Auth.auth()
        // Sets up message
        var message = "You have already signed out"
        // Signs out the user
        if let user = firebaseAuth.currentUser{
            do {
                try firebaseAuth.signOut()
                message = "You have signed out as " + user.displayName
            } catch let signOutError as NSError {
                message = String.init(format: "Error signing out: %@", signOutError)
            }
        }
        // Alterts result
        self.makeAlert(message: message)
    }
    
    /**
     * This method make a alter with OK button.
     * - Parameter message: A string mess to show in the alter
     */
    func makeAlert(message: String){
        // Make alert
        let alert = UIAlertController(title: "Message", message: message, preferredStyle: UIAlertControllerStyle.alert)
        // add an action (button)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        // show the alert
        self.present(alert, animated: true, completion: nil)
    }
}
