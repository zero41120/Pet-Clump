//
//  FrinedListViewVC.swift
//  PetClump
//
//  Created by Jerod Zheng on 5/20/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit

class FrinedListViewVC: UIViewController, UITableViewDelegate, UITableViewDataSource {

    let elements = ["horse", "cat", "dog", "potato","horse", "cat", "dog", "potato","horse", "cat", "dog", "potato"]
    
    @IBOutlet weak var tableView: UITableView!
    
    override func viewDidLoad() {
        
        tableView.delegate = self
        tableView.dataSource = self
        
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    
    
    public func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return elements.count
    }
    
    
    public func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "customCell") as! FriendListTableViewCell
        
        cell.animalLbl.text = elements[indexPath.row]
        cell.animalChat.text = elements[indexPath.row]
        cell.animalTime.text = elements[indexPath.row]
        cell.animalImage.image = UIImage(named: elements[indexPath.row])
        
        
        return cell
    }

}






