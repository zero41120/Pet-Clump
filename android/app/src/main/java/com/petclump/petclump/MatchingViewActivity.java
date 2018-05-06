package com.petclump.petclump;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.petclump.petclump.models.PetProfile;

import java.util.ArrayList;
import java.util.List;


/*******
 * * * *
 * * * *
 * * * *
 *******/
public class MatchingViewActivity extends AppCompatActivity {
    private static final Integer DEFAULT_DOWNLOAD_LIMIT = 30;
    private List<PetProfile> pets;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "MatchingViewActivity";
    private PetProfile profile;
    private Query petProfileQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);

        pets = new ArrayList<>();
        downloadPetProfiles();
//        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                recycleViewAdapter.notifyDataSetChanged();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });

    }
    private void setRecyclerView(){
        recyclerView = findViewById(R.id.matchviewRecycle);
        recycleViewAdapter = new RecycleViewAdapter(this, pets);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recycleViewAdapter);
    }

    private void downloadPetProfiles(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        petProfileQuery = db.collection("pets").limit(DEFAULT_DOWNLOAD_LIMIT);

        petProfileQuery.get().addOnSuccessListener(documentSnapshots -> {
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
            setRecyclerView();
            Log.d(TAG, pets.toString());
        });
    }

}
