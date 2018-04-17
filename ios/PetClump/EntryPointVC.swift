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
import FacebookCore
import FBSDKCoreKit
import FBSDKShareKit
import FBSDKLoginKit

class EntryPointVC: UIViewController, GIDSignInUIDelegate, FBSDKLoginButtonDelegate {
    
    @IBOutlet weak var signInButton: GIDSignInButton!
    @IBOutlet weak var signOutButton: UIButton!
    @IBOutlet weak var facebookLoginButtonView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Handels Google sign-in
        GIDSignIn.sharedInstance().uiDelegate = self
        // This auto-signs in
        // GIDSignIn.sharedInstance().signIn()
        
        // Creates Facebook sign-in
        let loginButton = FBSDKLoginButton()
        loginButton.delegate = self
        loginButton.center = facebookLoginButtonView.center
        view.addSubview(loginButton)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    /**
     * This method implements the protocal when the user clicks Facebook loggin button.
     */
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        // Prints error
        if let error = error {
            print(error.localizedDescription)
            return
        }
        // Checks if firebase has alreay authenticated a user
        if let user = Auth.auth().currentUser{
            self.makeAlert(message: "You have already signed in as " + user.uid)
            return
        }
        // Authenticates Firebase with the Facebook user
        let credential = FacebookAuthProvider.credential(withAccessToken: FBSDKAccessToken.current().tokenString)
        Auth.auth().signIn(with: credential) { (user, error) in
            if let error = error {
                print(error.localizedDescription)
                return
            }
            if let uid = user?.uid {
                self.makeAlert(message: "You have logged in with Facebook as " + uid);
            }
        }
    }
    
    /**
     * This method implements the protocal when the user clicks Facebook logout button.
     */
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        var message = "You have already signed out"
        if let uid = Auth.auth().currentUser?.uid{
            message = "You have logged out Facebook as " + uid
        }
        self.makeAlert(message: message);
    }
    
    /**
     * This action signs out any Firebase authenticated user if signed in, then alerts the result.
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
                message = "You have signed out as " + user.uid
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
