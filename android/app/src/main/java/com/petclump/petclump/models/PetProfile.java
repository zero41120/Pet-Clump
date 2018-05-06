package com.petclump.petclump.models;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petclump.petclump.ProfileDeletor;
import com.petclump.petclump.ProfileDownloader;
import com.petclump.petclump.ProfileUploader;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PetProfile implements Profile{
    private String bio = "PET_BIO";
    private String age = "0";
    private String spe = "CAT";
    private String name = "Guko";
    private String owner_id = "null";
    private Integer sequence = -1;
    private String TAG = "PetProfile";
/*    // pet photo
    private byte[] main_profile;
    private byte[] pet_profile_1;
    private byte[] pet_profile_2;
    private byte[] pet_profile_3;
    private byte[] pet_profile_4;
    private byte[] pet_profile_5;
    // group photo
    private byte[] group_profile_1;
    private byte[] group_profile_2;
    private byte[] group_profile_3;*/

    // pet photo url
    private String main_profile_url = "";
    private String pet_profile_url_1 = "";
    private String pet_profile_url_2 = "";
    private String pet_profile_url_3 = "";
    private String pet_profile_url_4 = "";
    private String pet_profile_url_5 = "";

    private String group_profile_url_1 = "";
    private String group_profile_url_2 = "";
    private String group_profile_url_3 = "";

    // firebase instance
    private FirebaseAuth Auth_pet = FirebaseAuth.getInstance();
    private FirebaseStorage Store_pet = FirebaseStorage.getInstance();

    public PetProfile (){}
    public Map<String,Object> generateDictionary(){
        return new HashMap<String, Object>(){{
            put("bio", bio);
            put("age", age);
            put("spe", spe);
            put("name",name);
            put("owner_id",owner_id);
            put("sequence",sequence);
            put("main_url", main_profile_url);
            put("pet_view_1", pet_profile_url_1);
            put("pet_view_2",pet_profile_url_2);
            put("pet_view_3",pet_profile_url_3);
            put("pet_view_4",pet_profile_url_4);
            put("pet_view_5",pet_profile_url_5);
            put("group_view_1",group_profile_url_1);
            put("group_view_2",group_profile_url_2);
            put("group_view_3",group_profile_url_3);
        }};
    }
    public void upload(String id, ProfileUploader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.w(TAG, "User is null! Cannot update.");
            return;
        }
        // upload documents
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("pets").document(id);
        docRef.set(generateDictionary()).addOnCompleteListener(task -> {
            String message = "";
            if(task.isSuccessful()) {
                message += "Upload successful for pet: " + this.name;
            }
            else {message += "Upload fails for pet " + this.name;}
            Log.d("Profile", "upload: " + message);
        });
    }
    @Override
    public void download(String id, ProfileDownloader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, " Current User is none");
            return;
        }
        DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("pets").document(id);

        mDocRef.addSnapshotListener((snap, error) -> {
            if (error != null) {
                Log.d(TAG, "Download failed: " + error.toString());
                return;
            }
            if (snap == null)   { return; }
            if (!snap.exists()) { return; }
            Map<String, Object> ref = snap.getData();
            this.bio = ref.get("bio").toString();
            this.age = ref.get("age").toString();
            this.spe = ref.get("spe").toString();
            this.name = ref.get("name").toString();
            this.owner_id = ref.get("owner_id").toString();
            this.sequence = Integer.parseInt(ref.get("sequence").toString());
            this.main_profile_url = ref.get("main_url").toString();
            this.pet_profile_url_1 = ref.get("pet_view_1").toString();
            this.pet_profile_url_2 = ref.get("pet_view_2").toString();
            this.pet_profile_url_3 = ref.get("pet_view_3").toString();
            this.pet_profile_url_4 = ref.get("pet_view_4").toString();
            this.pet_profile_url_5 = ref.get("pet_view_5").toString();
            this.group_profile_url_1 = ref.get("group_view_1").toString();
            this.group_profile_url_2 = ref.get("group_view_2").toString();
            this.group_profile_url_3 = ref.get("group_view_3").toString();

            c.didCompleteDownload();
        });
    }
    public void delete(String id, ProfileDeletor c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, " Current User is none");
            return;
        }
        FirebaseFirestore.getInstance().collection("pets").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        c.didCompleteDelete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });;
    }
    public void setPhoto(String t, byte[] data, ProfileUploader c){
        String path = "test/" + UUID.randomUUID() + ".png";
        StorageReference storageRef = Store_pet.getReference(path);
        // upload the file
        UploadTask uploadTask = storageRef.putBytes(data);
        // find the download url
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener((Task<Uri> task)->{
            String url = task.getResult().toString();
            Log.d(TAG,"URLTask:"+url);
            switch(t){
                case "main":
                    this.main_profile_url = url;
                    break;
                case "p1":
                    this.pet_profile_url_1 = url;
                    break;
                case "p2":
                    this.pet_profile_url_2 = url;
                    break;
                case "p3":
                    this.pet_profile_url_3 = url;
                    break;
                case "p4":
                    this.pet_profile_url_4 = url;
                    break;
                case "p5":
                    this.pet_profile_url_5 = url;
                    break;
                case "g1":
                    this.group_profile_url_1 = url;
                    break;
                case "g2":
                    this.group_profile_url_2 = url;
                    break;
                case "g3":
                    this.group_profile_url_3 = url;
                    break;
                default: Log.w(TAG,"setPhoto called on unknown tag "+t);
            }
        });

        c.didCompleteUpload();
    }
    public String getOwnerId() {
        return owner_id;
    }
    public String getName() {
        return name;
    }
    public String getSpe(){return spe;}
    public String getBio(){return bio;}
    public String getAge(){return age;}
    public String getMain_profile_url(){return main_profile_url;}
    public String getPet_profile_url_1(){return pet_profile_url_1;}
    public String getPet_profile_url_2(){return pet_profile_url_2;}
    public String getPet_profile_url_3(){return pet_profile_url_3;}
    public String getPet_profile_url_4(){return pet_profile_url_4;}
    public String getPet_profile_url_5(){return pet_profile_url_5;}
    public String getGroup_profile_url_1() {return group_profile_url_1;}
    public String getGroup_profile_url_2() {return group_profile_url_2;}
    public String getGroup_profile_url_3() {return group_profile_url_3;}
    public void setOwner_id(String owner_id){this.owner_id = owner_id;}
    public void setName(String name){this.name = name;}
    public void setSpe(String spe){this.spe = spe;}
    public void setBio(String bio){this.bio = bio;}
    public void setAge(String age){this.age = age;}
    public void setSequence(int s){this.sequence = s;}


}
