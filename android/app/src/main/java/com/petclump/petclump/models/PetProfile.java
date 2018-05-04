package com.petclump.petclump.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.ProfileDeletor;
import com.petclump.petclump.ProfileDownloader;
import com.petclump.petclump.ProfileUploader;

import java.util.HashMap;
import java.util.Map;

public class PetProfile implements Profile{
    private String bio = "PET_BIO";
    private String age = "0";
    private String spe = "CAT";
    private String name = "Guko";
    private String owner_id = "null";
    private Integer sequence = -1;
    private String TAG = "PetProfile";


    public PetProfile (){
    }

    public Map<String,Object> generateDictionary(){
        return new HashMap<String, Object>(){{
            put("bio", bio);
            put("age", age);
            put("spe", spe);
            put("name",name);
            put("owner_id",owner_id);
            put("sequence",sequence);
        }};


    }
    public void upload(String id, ProfileUploader c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            c.didCompleteUpload();
        }
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String TAG = "PetProfile_"+name;
        if (user == null){ c.didCompleteDownload(); return;}
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
            c.didCompleteDownload();
        });
    }
    public void delete(String id, ProfileDeletor c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d(TAG, " Current User is none");
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

    public String getOwnerId() {
        return owner_id;
    }
    public String getName() {
        return name;
    }
    public String getSpe(){return spe;}
    public String getBio(){return bio;}
    public String getAge(){return age;}
    public void setOwner_id(String owner_id){this.owner_id = owner_id;}
    public void setName(String name){this.name = name;}
    public void setSpe(String spe){this.spe = spe;}
    public void setBio(String bio){this.bio = bio;}
    public void setAge(String age){this.age = age;}
    public void setSequence(int s){this.sequence = s;}

}
