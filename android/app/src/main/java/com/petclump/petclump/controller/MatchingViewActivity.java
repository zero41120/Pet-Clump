package com.petclump.petclump.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.petclump.petclump.views.EndlessRecyclerViewScrollListener;
import com.petclump.petclump.R;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.views.RecycleViewAdapter;

import java.util.ArrayList;
import java.util.List;


/*******
 * * * *
 * * * *
 * * * *
 *******/
public class MatchingViewActivity extends AppCompatActivity {
    private static final Integer DEFAULT_DOWNLOAD_LIMIT = 6;
    private List<PetProfile> pets;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private GridLayoutManager gridLayoutManager;
    private FirebaseFirestore db;


    private static final String TAG = "MatchingViewActivity";
    private PetProfile profile;
    private Query petProfileQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);
        db = FirebaseFirestore.getInstance();
        petProfileQuery = db.collection("pets").limit(DEFAULT_DOWNLOAD_LIMIT);
        pets = new ArrayList<>();
        downloadPetProfiles(6);
        setRecyclerView();
//
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.matchviewRecycle);
        recycleViewAdapter = new RecycleViewAdapter(this, pets);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleViewAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                downloadPetProfiles(page);
                //Toast.makeText(this, "slide", Toast.LENGTH_SHORT).show();
                //Log.d(TAG,"SLIDE:"+"slide a little bit");
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    private void downloadPetProfiles(int offset){
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        petProfileQuery = db.collection("pets").limit(DEFAULT_DOWNLOAD_LIMIT);

        petProfileQuery.get().addOnSuccessListener(documentSnapshots -> {
            if(documentSnapshots.size() -1 < 0){ return; }
            DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                    .get(documentSnapshots.size() -1);
            for (DocumentSnapshot doc: documentSnapshots.getDocuments()) {
                doc.getData();
                Log.d(TAG, "downloadPetProfiles: " + doc.getId());
                pets.add(new PetProfile(){{
                    Log.d(TAG, doc.getData().get("name").toString());
                    setName(doc.getData().get("name").toString());
                    setAge(doc.getData().get("age").toString());
                    setBio(doc.getData().get("bio").toString());
                }});
            }

            petProfileQuery = db.collection("pets")
                    .startAfter(lastVisible)
                    .limit(DEFAULT_DOWNLOAD_LIMIT);
            //setRecyclerView();
            recycleViewAdapter.notifyItemInserted(6);
            //Log.d(TAG, pets.toString());

        });
    }

}
