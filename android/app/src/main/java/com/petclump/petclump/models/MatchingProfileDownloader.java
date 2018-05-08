package com.petclump.petclump.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

public class MatchingProfileDownloader {
    private static final String COLLECTION_NAME = "pets";
    private static final String TAG = "ProfileDownloader";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer downloadLimit = 2;
    private Query petProfileQuery;
    private PetProfile pet;

    public MatchingProfileDownloader(PetProfile myPet, Integer downloadLimit){
        this.downloadLimit = downloadLimit;
        this.pet = myPet;
        petProfileQuery = db.collection(COLLECTION_NAME).limit(downloadLimit);
    }

    public void downloadMore(List<MatchingProfile> toAppend, ProfileDownloader c){
        List<MatchingProfile> profiles = new ArrayList<>();
        petProfileQuery.get().addOnSuccessListener(documentSnapshots -> {
            if(documentSnapshots.size() -1 < 0){ return; }
            DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                    .get(documentSnapshots.size() -1);
            for (DocumentSnapshot doc: documentSnapshots.getDocuments()) {
                PetProfile petInfo = new PetProfile(doc.getData());
                MatchingProfile match = new MatchingProfile(this.pet.getQuiz(), petInfo);
                profiles.add(match);
            }
            Log.d(TAG, "Completed: " + profiles);
            petProfileQuery = db.collection(COLLECTION_NAME)
                    .startAfter(lastVisible).limit(downloadLimit);
            Collections.sort(profiles);
            toAppend.addAll(profiles);
            c.didCompleteDownload();
        });
    }
}

