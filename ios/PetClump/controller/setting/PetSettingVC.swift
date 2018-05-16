//
//  PetDataViewVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class PetSettingVC: UIViewController{
    //Title Labels
    @IBOutlet weak var nameTitleLabel:    UILabel!
    @IBOutlet weak var infoTitleLabel:    UILabel!
    @IBOutlet weak var petSpeciesLabel:   UILabel!
    @IBOutlet weak var petAgeTitleLabel:  UILabel!
    @IBOutlet weak var petBioTitleLabel:  UILabel!
    @IBOutlet weak var bioRemainingLabel: UILabel!
    @IBOutlet weak var petNameTitleLabel: UILabel!
    @IBOutlet weak var groupPhotoLabel:   UILabel!

    //Information display
    @IBOutlet weak var scrollView: UIScrollView!
    @IBOutlet weak var petBioTextView:      UITextView!
    @IBOutlet weak var petAgeTextField:     UITextField!
    @IBOutlet weak var petNameTextField:    UITextField!
    @IBOutlet weak var petSpeciesTextField: UITextField!
    
    //Pet pictures display
    @IBOutlet weak var bigPetPicture0:   UIImageView!
    @IBOutlet weak var smallPetPicture1: UIImageView!
    @IBOutlet weak var smallPetPicture2: UIImageView!
    @IBOutlet weak var smallPetPicture3: UIImageView!
    @IBOutlet weak var smallPetPicture4: UIImageView!
    @IBOutlet weak var smallPetPicture5: UIImageView!
    @IBOutlet weak var petAndOwnerPic6: UIImageView!
    @IBOutlet weak var petAndOwnerPic7: UIImageView!
    @IBOutlet weak var petAndOwnerPic8: UIImageView!

    // Buttons
    @IBOutlet weak var quizButton:   UIButton!
    @IBOutlet weak var saveButton:   UIBarButtonItem!
    @IBOutlet weak var exitButton:   UIBarButtonItem!
    @IBOutlet weak var deleteButton: UIButton!
    
    // Assigned by caller view
    var petProfile:         PetProfile?
    var speciePicker:       UIPickerView?
    var ageInputDelegate:   UITextFieldDelegate?
    var nameInputDelegate:  UITextFieldDelegate?
    var speciePickerDelegate: SpecieInputDelegate?
    var remainingBioDelegate: UITextViewDelegate?
    var imagePickerDelegate:  ImageInputDelegate?
    
    
    @objc func keyboardWillShow(notification:NSNotification){
        
        var userInfo = notification.userInfo!
        var keyboardFrame:CGRect = (userInfo[UIKeyboardFrameBeginUserInfoKey] as! NSValue).cgRectValue
        keyboardFrame = self.view.convert(keyboardFrame, from: nil)
        
        var contentInset:UIEdgeInsets = self.scrollView.contentInset
        contentInset.bottom = keyboardFrame.size.height
        self.scrollView.contentInset = contentInset
        scrollView.contentInset = UIEdgeInsetsMake(0, 0, keyboardFrame.height, 0)

    }
    
    @objc func keyboardWillHide(notification:NSNotification){
        
        let contentInset:UIEdgeInsets = UIEdgeInsets.zero
        self.scrollView.contentInset = contentInset
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        guard (Auth.auth().currentUser != nil) else {
            self.dismiss(animated: true, completion: nil)
            return
        }
        hideKeyboardWhenTappedAround()
        
        // Static UI
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillShow), name:NSNotification.Name.UIKeyboardWillShow, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(keyboardWillHide), name:NSNotification.Name.UIKeyboardWillHide, object: nil)
        nameTitleLabel.text        = NSLocalizedString("Pet Name", comment: "This is the title for specifying the name of the pet")
        infoTitleLabel.text        = NSLocalizedString("INFO", comment: "This is the title for the section of the pet information")
        petSpeciesLabel.text       = NSLocalizedString("Species", comment: "This is the title for specifying the species of the pet")
        petAgeTitleLabel.text      = NSLocalizedString("Age", comment: "This is the title for specifying the age of the pet")
        petBioTitleLabel.text      = NSLocalizedString("Bio", comment: "This is the title for specifying the Bio of the pet")
        petNameTitleLabel.text     = NSLocalizedString("Name", comment: "This is the title for specifying the name of the pet in the pet info section")
        groupPhotoLabel.text = NSLocalizedString("Pet And I", comment: "This is the title for the picture section of the pet and owner")
        quizButton.titleLabel!.text = NSLocalizedString("Start Quiz", comment: "This is the title for the quiz button when the user finished all quiz questions")
    
        // Specie picker
        speciePicker = UIPickerView()
        speciePickerDelegate = SpecieInputDelegate(textField: petSpeciesTextField)
        speciePicker!.delegate = speciePickerDelegate
        speciePicker!.dataSource = speciePickerDelegate
        petSpeciesTextField.delegate = speciePickerDelegate
        petSpeciesTextField.inputView = speciePicker
        
        // Text fields and textview delegates
        nameInputDelegate = LimitTextFieldDelegate(count: 20)
        ageInputDelegate  = LimitTextFieldDelegate(count: 50)
        remainingBioDelegate = LimitTextViewDelegate(count: 500, remainingLable: bioRemainingLabel)
        petBioTextView.makeTextField(delegate: remainingBioDelegate!)
        petNameTextField.delegate = nameInputDelegate
        petAgeTextField.delegate  = ageInputDelegate
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        // Sequence of pet is set by the UserDataView
        petProfile!.download {
            self.didCompleteDownload()
        }
    }
    
    func didCompleteDownload() {
        // Text downloaded
        petBioTextView.text         = petProfile!.bio
        petAgeTextField.text        = petProfile!.age
        petNameTextField.text       = petProfile!.name
        bioRemainingLabel.text      = "\(petProfile!.bio.count)/500"
        petSpeciesTextField.text    = petProfile!.specie
        
        let quizText =
            QuizQuestion.isAllDone(quiz: petProfile!.quiz) ?
                NSLocalizedString("All Quiz Done", comment: "This is the button that takes the user to quiz view. It shows all quiz completed for this particular pet") :
                NSLocalizedString("Quiz (\(petProfile!.quiz.count)/\(QuizQuestion.getNumberOfAvaliableQuestions()))", comment: "This is the button that takes the user to quiz view. It shows how many quiz this user has complete for this particular pet")
        quizButton.setTitle(quizText, for: UIControlState.normal)
        deleteButton.addTarget(self, action: #selector(deletePet), for: .touchUpInside)
        
        // Load pictures with url
        bigPetPicture0.load(url:   petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.main))
        smallPetPicture1.load(url: petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet1))
        smallPetPicture2.load(url: petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet2))
        smallPetPicture3.load(url: petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet3))
        smallPetPicture4.load(url: petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet4))
        smallPetPicture5.load(url: petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet5))
        petAndOwnerPic6.load(url:  petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.group1))
        petAndOwnerPic7.load(url:  petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.group2))
        petAndOwnerPic8.load(url:  petProfile!.getPhotoUrl(key: PetProfile.PetPhotoUrlKey.group3))
    }
    
    @objc private func deletePet(){
        confirmBefore(deleting: petProfile!,
                      title: NSLocalizedString("Delete this pet?", comment: "This is the title on an alert when user clicks delete pet"),
                      message: NSLocalizedString("Are you sure you want to delete this pet? This action cannot be undone.", comment: "This is the message when the user clicks delete pet"))
    }
    
    @IBAction func tapOnImageView(_ sender: UITapGestureRecognizer) {
        let imageView = sender.view as! UIImageView
        
        // Action sheet to prompt user to upload / delete photo
        let alert = UIAlertController(title: NSLocalizedString("Image Function", comment: "This is an alert title when user clicks on the image to upload or delete"), message: "", preferredStyle: UIAlertControllerStyle.actionSheet)
        // Upload image
        alert.addAction(UIAlertAction(title: NSLocalizedString("Upload a new image", comment: "This is a button to indicate user to upload a new image."), style: .default, handler: { (action: UIAlertAction!) in
            self.imagePickerDelegate = ImageInputDelegate(imageView: imageView, profile: self.petProfile!)
            let imagePicker = UIImagePickerController()
            imagePicker.sourceType = .photoLibrary
            imagePicker.delegate = self.imagePickerDelegate!
            self.present(imagePicker, animated: true, completion: nil)
        }))
        // Delete image
        alert.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { (action: UIAlertAction!) in
            imageView.image = nil;
            imageView.backgroundColor = UIImageView.getDefaultDeselectedColor()
            switch imageView.tag {
            case 0: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.main, url: "")
            case 1: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet1, url: "")
            case 2: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet2, url: "")
            case 3: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet3, url: "")
            case 4: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet4, url: "")
            case 5: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.pet5, url: "")
            case 6: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group1, url: "")
            case 7: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group2, url: "")
            case 8: self.petProfile!.setPhotoUrl(key: PetProfile.PetPhotoUrlKey.group3, url: "")
            default: break
            }
            self.petProfile?.upload(vc: nil, completion: nil)
        }))
        // Cancel
        alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
        self.present(alert, animated: true)
    }
    
    @IBAction func tapSave(_ sender: Any) {
        if let uid = Auth.auth().currentUser?.uid{
            petProfile!.name    = petNameTextField.text!
            petProfile!.bio     = petBioTextView.text!
            petProfile!.age     = petAgeTextField.text!
            petProfile!.specie  = petSpeciesTextField.text!
            petProfile!.ownerId = uid
            petProfile!.upload(vc: self, completion: nil)
            self.makeAlert(message: NSLocalizedString("Your pet information is saved!", comment: "This is a alter message that shows up and the user tap save on the pet information viewing page."))
        }
    }
    
    @IBAction func tapQuiz(_ sender: Any){
        guard let uid = Auth.auth().currentUser?.uid else { return }
        
        // Hack, since normal user can only update 10 question at a time, so add 5 will remove the array index issue.
        if QuizQuestion.isAllDone(quiz: petProfile!.quiz){
            makeAlert(message: NSLocalizedString("You have complete all the questions!", comment: "This is an alert message when the user clicks the Start Quiz button but have finished all 100 questions"))
            return;
        }
        
        // Present quiz view
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let qvc = storyBoard.instantiateViewController(withIdentifier: "QuizVC") as! QuizVC
        // Send petprofile to quiz view
        qvc.petProfile = PetProfile()
        qvc.petProfile!.ownerId = uid
        qvc.petProfile!.sequence = self.petProfile!.sequence
        self.present(qvc, animated: true, completion: nil)
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
