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
    
    @IBAction func topSignOut(_ sender: Any) {
        do {
            let firebaseAuth = Auth.auth()
            var message = "You have already signed out"
            if let user = firebaseAuth.currentUser{
                message = "You have signed out as " + user.displayName
            }
            try firebaseAuth.signOut()
            self.makeAlert(message: message)
        } catch let signOutError as NSError {
            print ("Error signing out: %@", signOutError)
            self.makeAlert(message: "Signed out error!")
        }
    }
    override func viewDidLoad() {
        super.viewDidLoad()
        GIDSignIn.sharedInstance().uiDelegate = self
        GIDSignIn.sharedInstance().signIn()

    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    func makeAlert(message: String){
        // Make alert
        let alert = UIAlertController(title: "Message", message: message, preferredStyle: UIAlertControllerStyle.alert)
        
        // add an action (button)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        
        // show the alert
        self.present(alert, animated: true, completion: nil)
        
    }
}
