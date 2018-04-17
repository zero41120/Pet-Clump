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

class EntryPointVC: UIViewController, GIDSignInUIDelegate, FBSDKLoginButtonDelegate, GIDSignInDelegate{
    
    @IBOutlet weak var signInButton: GIDSignInButton!
    @IBOutlet weak var signOutButton: UIButton!
    @IBOutlet weak var facebookLoginButtonView: UIView!
    
    let debugMode = true
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Handels Google sign-in
        GIDSignIn.sharedInstance().clientID = FirebaseApp.app()?.options.clientID
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        // GIDSignIn.sharedInstance().signIn() // This auto-signs in
        
        // Creates Facebook sign-in
        let loginButton = FBSDKLoginButton()
        loginButton.delegate = self
        loginButton.center = facebookLoginButtonView.center
        facebookLoginButtonView.backgroundColor = .clear
        view.addSubview(loginButton)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    @available(iOS 9.0, *)
    func application(_ application: UIApplication, open url: URL, options: [UIApplicationOpenURLOptionsKey : Any])
        -> Bool {
            return GIDSignIn.sharedInstance().handle(url,
                                                     sourceApplication:options[UIApplicationOpenURLOptionsKey.sourceApplication] as? String,
                                                     annotation: [:])
    }
    
    /**
     * This method implements the protocal when the user clicks Google logout button.
     */
    func sign(_ signIn: GIDSignIn!, didSignInFor user: GIDGoogleUser!, withError error: Error?) {
        if isSignedIn(alertUid: debugMode){ return }
        if let error = error {
            print(error.localizedDescription)
            return
        }
        
        // Authenticates Firebase with the Google user
        guard let authentication = user.authentication else { return }
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken,
                                                       accessToken: authentication.accessToken)
        Auth.auth().signIn(with: credential) { (user, error) in
            if let error = error {
                print(error.localizedDescription)
                return
            }
            if let uid = user?.uid{
                self.makeAlert(message: "You have logged in with Google as " + uid);
            }
        }
    }
    
    func sign(_ signIn: GIDSignIn!, didDisconnectWith user: GIDGoogleUser!, withError error: Error!) {
        // Perform any operations when the user disconnects from app here.
        // ...
    }
    
    
    // ===============
    //    Facebook
    // ===============
    /**
     * This method implements the protocal when the user clicks Facebook loggin button.
     */
    func loginButton(_ loginButton: FBSDKLoginButton!, didCompleteWith result: FBSDKLoginManagerLoginResult!, error: Error!) {
        if isSignedIn(alertUid: debugMode){ return }
        if let error = error {
            print(error.localizedDescription)
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
    
    // ===============
    //    Common
    // ===============
    
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
     * This method checks if Firebase is authenicated by a user account.
     * - Returns: ture if login
     */

    func isSignedIn(alertUid: Bool) -> Bool {
        // Checks if firebase has alreay authenticated a user
        if let user = Auth.auth().currentUser{
            if alertUid {
                self.makeAlert(message: "You have already signed in as " + user.uid)
            }
            return true
        }
        return false
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
