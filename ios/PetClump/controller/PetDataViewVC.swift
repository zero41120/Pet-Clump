//
//  PetDataViewVC.swift
//  PetClump
//
//  Created by YSH on 4/27/18.
//  Copyright © 2018 PetClump. All rights reserved.
//

import UIKit
import Firebase

class PetDataViewVC: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate, ProfileDownloader{
    //Title Labels
    @IBOutlet weak var nameTitleLabel:          UILabel!
    @IBOutlet weak var petAndOwnerTitleLabel:   UILabel!
    @IBOutlet weak var infoTitleLabel:          UILabel!
    @IBOutlet weak var petNameTitleLabel:       UILabel!
    @IBOutlet weak var petSpeciesLabel:         UILabel!
    @IBOutlet weak var petAgeTitleLabel:        UILabel!
    @IBOutlet weak var petBioTitleLabel:        UILabel!
    @IBOutlet weak var bioRemainingLabel:       UILabel!

    //Information display
    @IBOutlet weak var petNameTextField:    UITextField!
    @IBOutlet weak var petSpeciesTextField: UITextField!
    @IBOutlet weak var petAgeTextField:     UITextField!
    @IBOutlet weak var petBioTextView:      UITextView!
    //Pet pictures display
    @IBOutlet weak var bigPetPicture0:    UIImageView!
    @IBOutlet weak var smallPetPicture1: UIImageView!
    @IBOutlet weak var smallPetPicture2: UIImageView!
    @IBOutlet weak var smallPetPicture3: UIImageView!
    @IBOutlet weak var smallPetPicture4: UIImageView!
    @IBOutlet weak var smallPetPicture5: UIImageView!
    //Pet and Owner Pictures display
    @IBOutlet weak var petAndOwnerPic6: UIImageView!
    @IBOutlet weak var petAndOwnerPic7: UIImageView!
    @IBOutlet weak var petAndOwnerPic8: UIImageView!

    @IBOutlet weak var quizButton: UIButton!

    var petProfile:     PetProfile?
    var speciePicker:   UIPickerView?
    var ageInputDelegate:   UITextFieldDelegate?
    var nameInputDelegate:  UITextFieldDelegate?
    var speciePickerDelegate: SpeciePicker?
    var remainingBioDelegate: UITextViewDelegate?
    
    //variable for the image tag
    var imageTag = 0
    
    func uploadImageToFirebaseStorage(data: NSData){
        //upload to firebase
        let fileName = NSUUID.init().uuidString + ".png"
        let storageRef = Storage.storage().reference(withPath: "test/\(fileName)")
        let uploadMetaData = StorageMetadata()
        uploadMetaData.contentType =  "image/png"
        let uploadTask = storageRef.putData(data as Data, metadata: uploadMetaData) {
            (metadata, error) in if (error != nil) {
                print("I received an error! \(String(describing: error?.localizedDescription))")
            } else {
                storageRef.downloadURL(completion: { (url, error) in
                    if error != nil {
                        print("\(error)")
                    } else {
                        print("\(url)")
                    }
                })
            }
        }
        /**
        uploadTask.oberserveStatus(.progress) { [weak self] (snapshot) in
            guard let strongSelf = self else { return }
            guard let progress = snapshot.progress else { return }
            strongSelf.progressView.progress = Float(progress.fractionCompleted)
        }
 **/
 
    }
    
    func imagePickerControllerDidCancel(_ picker: UIImagePickerController) {
        dismiss(animated: true, completion: nil)
    }
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        guard let mediaType: String = info[UIImagePickerControllerMediaType] as? String else {
            dismiss(animated: true, completion: nil)
            return
        }
        if let originalImage = info[UIImagePickerControllerOriginalImage] as? UIImage, let imageData = UIImageJPEGRepresentation(originalImage, 0.8) {
            uploadImageToFirebaseStorage(data: imageData as NSData)
            
        }
        dismiss(animated: true, completion: nil)
        
    }
    
    @IBAction func tapToUploadImage(_ sender: UITapGestureRecognizer) {
        let imagePicker = UIImagePickerController()
        imagePicker.sourceType = .photoLibrary
        //UIGestureRecognizer; *recognizer = (UIGestureRecognizer*)sender
        //if let view = gestureRecogniser.view as? UIImageView{
        //let imagePicked = view.tag
        //}
        //print("tapped view is view with tag: \(sender.view!!.tag)")
        let image = sender.view
        let imageTag = image?.tag
        imagePicker.delegate = self
        present(imagePicker, animated: true, completion: nil)
    }
    
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        if let _ = Auth.auth().currentUser?.uid {
            self.setupUI()
            setupDelegate()
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        guard let uid = Auth.auth().currentUser?.uid else { return }
        petProfile!.download(uid: uid, completion: self)
    }
    
    // Pre-fill text fields when pet info is downloaded from Firebase
    func didCompleteDownload() {
        self.petBioTextView.text        = petProfile!.bio
        self.petAgeTextField.text       = petProfile!.age
        self.petNameTextField.text      = petProfile!.name
        self.bioRemainingLabel.text     = "\(petProfile!.bio.count)/500"
        self.petSpeciesTextField.text   = petProfile!.specie
        self.quizButton.titleLabel!.text = NSLocalizedString("Start Quiz (\(petProfile!.quiz.count)/100)", comment: "This is the button that takes the user to quiz view. It shows how many quiz this user has complete for this particular pet")
    }
    
    private func setupUI(){
        self.nameTitleLabel.text        = NSLocalizedString("Pet Name", comment: "This is the title for specifying the name of the pet")
        self.infoTitleLabel.text        = NSLocalizedString("INFO", comment: "This is the title for the section of the pet information")
        self.petSpeciesLabel.text       = NSLocalizedString("Species", comment: "This is the title for specifying the species of the pet")
        self.petAgeTitleLabel.text      = NSLocalizedString("Age", comment: "This is the title for specifying the age of the pet")
        self.petBioTitleLabel.text      = NSLocalizedString("Bio", comment: "This is the title for specifying the Bio of the pet")
        self.petNameTitleLabel.text     = NSLocalizedString("Name", comment: "This is the title for specifying the name of the pet in the pet info section")
        self.petAndOwnerTitleLabel.text = NSLocalizedString("Pet And I", comment: "This is the title for the picture section of the pet and owner")
    }
    
    private func setupDelegate(){
        // Specie picker
        speciePicker = UIPickerView()
        speciePickerDelegate = SpeciePicker(textField: petSpeciesTextField)
        speciePicker!.delegate = speciePickerDelegate
        speciePicker!.dataSource = speciePickerDelegate
        petSpeciesTextField.delegate = speciePickerDelegate
        petSpeciesTextField.inputView = speciePicker
        
        // Text fields and textview delegates
        nameInputDelegate = LimitTextFieldInput(count: 20)
        ageInputDelegate  = LimitTextFieldInput(count: 50)
        remainingBioDelegate = LimitTextViewInput(count: 500, remainingLable: bioRemainingLabel)
        petBioTextView.makeTextField(delegate: remainingBioDelegate!)
        petNameTextField.delegate = nameInputDelegate
        petAgeTextField.delegate  = ageInputDelegate
    }
    
    
    @IBAction func tapSave(_ sender: Any) {
        if let uid = Auth.auth().currentUser?.uid{
            petProfile!.name = petNameTextField.text!
            petProfile!.bio  = petBioTextView.text!
            petProfile!.age  = petAgeTextField.text!
            petProfile!.specie = petSpeciesTextField.text!
            petProfile!.ownerId = uid
            petProfile!.upload(vc: self, completion: nil)
            self.makeAlert(message: NSLocalizedString("Your pet information is saved!", comment: "This is a alter message that shows up and the user tap save on the pet information viewing page."))
        }
        
    }
    
    @IBAction func tapQuiz(_ sender: Any){
        if petProfile!.quiz.count == 100 {
            makeAlert(message: NSLocalizedString("You have complete all the questions!", comment: "This is an alert message when the user clicks the Start Quiz button but have finished all 100 questions"))
        }
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let qvc = storyBoard.instantiateViewController(withIdentifier: "QuizVC") as! QuizVC
        qvc.petProfile = PetProfile()
        qvc.petProfile!.sequence = self.petProfile!.sequence
        self.present(qvc, animated: true, completion: nil)
    }
    
    @IBAction func tapExit(_ sender: Any) {
        self.dismiss(animated: true, completion: nil)
    }
}
