package com.petclump.petclump.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.ProfileUpdator;

import java.util.HashMap;
import java.util.Map;

public class PetProfile implements Profile{
    private String bio;
    private String age;
    private String spe;
    private String name;
    private String owner_id;
    private Map<String, Object> ref = null;

    public PetProfile (){
    }

    public Map<String,Object> generateDictionary(){
        Map<String, Object> temp= new HashMap<>();
        temp.put("bio", bio);
        temp.put("age", age);
        temp.put("spe", spe);
        temp.put("name",name);
        temp.put("owner_id",owner_id);
        return temp;
    }
    public void upload(String id, Context c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(c, "PetProfile.upload: User not signed in!\n", Toast.LENGTH_SHORT).show();
        }
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("pets").document();
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
    public void download(String id, ProfileUpdator c){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String TAG = "PetProfile_"+name;
        if (user == null){ return; }
        String uid = user.getUid();
        DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("pets").document(uid);

        mDocRef.addSnapshotListener((snap, error) -> {
            if (error != null) {
                Log.d(TAG, "Download failed: " + error.toString());
                return;
            }
            if (snap == null)   { return; }
            if (!snap.exists()) { return; }
            this.ref = snap.getData();
            this.bio = ref.get("bio").toString();
            this.age = ref.get("age").toString();
            this.spe = ref.get("spe").toString();
            this.name = ref.get("name").toString();
            this.owner_id = ref.get("owner_id").toString();
            c.onComplete();
        });

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

}
