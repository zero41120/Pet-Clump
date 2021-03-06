//
//  SimpleAlert.swift
//  PetClump
//
//  Created by YSH on 4/22/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import Foundation
import UIKit
import UserNotifications

protocol QuickAlert {
    /**
     * This method make a alter with OK button.
     * - Parameter message: A string mess to show in the alter
     */
    func makeAlert(message: String)
}

protocol ConfirmDismissAlert{
    func confirmBeforeDismiss(title: String, message: String)
}

// https://stackoverflow.com/questions/37946990/cgrectmake-cgpointmake-cgsizemake-cgrectzero-cgpointzero-is-unavailable-in-s
extension CGRect{
    init(_ x:CGFloat,_ y:CGFloat,_ width:CGFloat,_ height:CGFloat) {
        self.init(x:x,y:y,width:width,height:height)
    }
}

/**
 * This estension makes the UIViewController dismiss keyboard when touch non-keyboard area
 */
extension UIViewController: QuickAlert, ConfirmDismissAlert {
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(UIViewController.dismissKeyboard))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
    
    func makeAlert(message: String){
        // Make alert
        let alert = UIAlertController(title: "Message", message: message, preferredStyle: UIAlertControllerStyle.alert)
        // add an action (button)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        // show the alert
        self.present(alert, animated: true, completion: nil)
    }
    
    func confirmBeforeDismiss(title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Leave", comment: "This is the Leave button on an alert to inform user that by click this button informaion on this page will not be saved"), style: .destructive, handler: { (action: UIAlertAction!) in
            self.dismiss(animated: true, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "This is the Cancel button on an alert to inform user that by clickign this button information on this page stays and user may contiune editing"), style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func confirmBefore(deleting: Deletable, title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Delete", comment: "This is the Leave button on an alert to inform user that by click this button informaion on this page will be deleted"), style: .destructive, handler: { (action: UIAlertAction!) in
            deleting.delete()
            self.dismiss(animated: true, completion: nil)
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "This is the Cancel button on an alert to inform user that by clickign this button information on this page stays and user may contiune editing"), style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func confirmBefore(doing: @escaping (()->Void), title: String, message: String) {
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: NSLocalizedString("Confirm", comment: "This is the Confirm button on an alert to inform user that by click this button informaion associate with this alert will be processed"), style: .destructive, handler: { (action: UIAlertAction!) in
            doing()
        }))
        alert.addAction(UIAlertAction(title: NSLocalizedString("Cancel", comment: "This is the Cancel button on an alert to inform user that by clickign this button information on this page stays and user may contiune editing"), style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    static private let cache = NSCache<NSString, NSString>()

    func showNotification(title: String = "Pet Clump", subtitle: String, message: String){
        let content = UNMutableNotificationContent()
        content.title = title
        content.subtitle = subtitle
        content.body = message
        if UIViewController.cache.object(forKey: NSString(string: message)) != nil {
            return
        }
        UIViewController.cache.setObject(NSString(string: subtitle), forKey: NSString(string: message))
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 1, repeats: false)
        let request = UNNotificationRequest(identifier: "timeDone", content: content, trigger: trigger)

        if UNUserNotificationCenter.current().delegate == nil{
            UNUserNotificationCenter.current().delegate = UIApplication.shared.delegate as! AppDelegate
        }
        UNUserNotificationCenter.current().add(request) { (error) in
            if let err = error { print(err) } 
        }
    }
}
