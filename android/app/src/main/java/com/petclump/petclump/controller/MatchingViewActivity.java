package com.petclump.petclump.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.petclump.petclump.models.MatchingProfile;
import com.petclump.petclump.models.MatchingProfileDownloader;
import com.petclump.petclump.models.protocols.ProfileDownloader;
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
public class MatchingViewActivity extends AppCompatActivity implements ProfileDownloader{
    private static final Integer DEFAULT_DOWNLOAD_LIMIT = 30;
    private static final String TAG = "Matching View Activity";

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private GridLayoutManager gridLayoutManager;


    private MatchingProfileDownloader md;
    private MatchingViewActivity self;
    private List<MatchingProfile> profiles;
    private PetProfile profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);
        self = this;
        profiles = new ArrayList<>();

        String petId = this.getIntent().getStringExtra("petId");
        profile = new PetProfile();
        profile.download(petId, ()->{
            // Make sure profile is downloaded before requesting profile
            md = new MatchingProfileDownloader(profile, DEFAULT_DOWNLOAD_LIMIT);
            setRecyclerView();
        });
    }

    private void setRecyclerView(){
        recyclerView = findViewById(R.id.matchviewRecycle);
        recycleViewAdapter = new RecycleViewAdapter(this, profiles);
        gridLayoutManager = new GridLayoutManager(self, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleViewAdapter);
        md.downloadMore(profiles, this);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                md.downloadMore(profiles, self);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void didCompleteDownload() {
        Integer lastSize = profiles.size();
        Log.d(TAG, "didCompleteDownload: "+ profiles);
        recycleViewAdapter.notifyItemInserted(lastSize);
    }
}
