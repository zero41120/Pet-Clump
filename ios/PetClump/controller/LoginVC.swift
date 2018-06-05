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
import BigInt

class LoginVC: GeneralVC, GIDSignInUIDelegate, FBSDKLoginButtonDelegate, GIDSignInDelegate{

    
    @IBOutlet weak var signInButtonGoogle: GIDSignInButton!
    @IBOutlet weak var facebookLoginButtonView: UIView!
    @IBOutlet weak var uidLabel: UILabel!
    var signInButtonFacebook: FBSDKLoginButton?
    
    let debugMode = true
    
    func unitTest(){
        let cG = Cryptographer.getInstance()
        let alice = KeyExchanger(friendId: "bobid")
        let bigPrime: BigInt = alice.getBigPrime()
        let priPrime: BigInt = alice.getPrimitiveRoot()
        let alicePublic = alice.getMyPublic()
        
        let bob = KeyExchanger(friendId: "aliceid", friendPublic: alicePublic, bigPrime: bigPrime, primitiveRoot: priPrime)
        
        let bobPublic: BigInt = bob.getMyPublic()
        
        let bobShared: Array<UInt8> = bob.getSharedKey(fdPublic: alicePublic)
        let aliceShared: Array<UInt8> = alice.getSharedKey(fdPublic: bobPublic)
        
        if bobShared == aliceShared && bobShared != [] {
            let key = bobShared

            let iv = cG.generateInitializationVector()
            let inputText = "Hello world 🍚"
            let cipherText = cG.encrypt(key: key, iv: iv, plainText: inputText)
            let plainText = cG.decrypt(key: key, iv: iv, cipherText: cipherText)
            print("crypto \(inputText) \(cipherText) : \(plainText)")
            
            for i in 0..<bobShared.count {
                print(String(bobShared[i]))
            }
            for i in 0..<aliceShared.count {
                print(String(aliceShared[i]))
            }        }

        
           }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        unitTest()
        
        // Handels Google sign-in
        GIDSignIn.sharedInstance().clientID = FirebaseApp.app()?.options.clientID
        GIDSignIn.sharedInstance().delegate = self
        GIDSignIn.sharedInstance().uiDelegate = self
        
        // Creates Facebook sign-in
        signInButtonFacebook = FBSDKLoginButton()
        if let btn = signInButtonFacebook{
            btn.delegate = self
            btn.center = facebookLoginButtonView.center
            facebookLoginButtonView.backgroundColor = .clear
            view.addSubview(btn)
        }
    }
    
    override func viewDidAppear(_ animated: Bool) {
        transitionLoginStateUI()
    }

    /**
     * This method sets up UI after login state change
     */
    func transitionLoginStateUI(){
        if let user = Auth.auth().currentUser {
            view.backgroundColor = .green
            uidLabel.text = user.uid
            
            let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let pdv = storyBoard.instantiateViewController(withIdentifier: "WelcomeVC") as! WelcomeVC
            self.present(pdv, animated: false, completion: nil)
        } else {
            view.backgroundColor = .yellow
            uidLabel.text = "You are not logged in"
        }
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
    
    func isSignedIn() -> Bool{
        return isSignedIn(alertUid: false)
    }
    
    /**
     * This method authenticates Firebase with the user credential.
     */
    func authenticateFirebase(credential: AuthCredential, withError error: Error? ){
        // Error checking
        if isSignedIn(alertUid: false){
            self.transitionLoginStateUI()
            return
            
        }
        if let error = error {
            print(error.localizedDescription)
            return
        }
        
        var message = "Sign in failed"
        Auth.auth().signIn(with: credential) { (user, error) in
            if let error = error {
                message = error.localizedDescription
            }
            if let uid = user?.uid{
                message = "You have logged in as " + uid
            }
            self.transitionLoginStateUI()
            self.makeAlert(message: message)
        }
    }
    
    // ===============
    //     Google
    // ===============
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
        
        // Authenticates Firebase with the Google user
        guard let authentication = user.authentication else { return }
        let credential = GoogleAuthProvider.credential(withIDToken: authentication.idToken,
                                                       accessToken: authentication.accessToken)
        authenticateFirebase(credential: credential, withError: error)
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
        // Authenticates Firebase with the Facebook user
        guard (FBSDKAccessToken.current() != nil) else { return }
        let credential = FacebookAuthProvider.credential(withAccessToken: FBSDKAccessToken.current().tokenString)
        authenticateFirebase(credential: credential, withError: error)
    }
    
    /**
     * This method implements the protocal when the user clicks Facebook logout button.
     */
    func loginButtonDidLogOut(_ loginButton: FBSDKLoginButton!) {
        var message = "You have already signed out"
        if let uid = Auth.auth().currentUser?.uid{
            message = "You have logged out Facebook as " + uid
        }
        makeAlert(message: message);
        transitionLoginStateUI()
    }
}
