package com.petclump.petclump.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FriendListDownloader {
    private static final String COLLECTION_NAME = "pets";
    private static final String TAG = "FriendListDownloader";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<PetProfile> friend_profiles;
    private ArrayList<Task<DocumentSnapshot>> check;

    public FriendListDownloader(ArrayList<String> friend_list, ProfileDownloader c){
        for(String friend_id: friend_list){
            Task<DocumentSnapshot> t = db.collection(COLLECTION_NAME).document(friend_id).get();
            check.add(t);
            t.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    friend_profiles.add(new PetProfile(task.getResult().getData()));

                }
            });
        }
        Tasks.whenAll(check).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                c.didCompleteDownload();
            }
        });
    }

    public ArrayList<PetProfile> getFriend_profiles(){
        return (ArrayList<PetProfile>) friend_profiles.clone();
    }
}
