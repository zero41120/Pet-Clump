package com.petclump.petclump.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petclump.petclump.R;
import com.petclump.petclump.models.MatchingProfile;
import com.petclump.petclump.models.MatchingProfileDownloader;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class BestMatchFragment extends Fragment implements ProfileDownloader {
    private static final Integer DEFAULT_DOWNLOAD_LIMIT = 30;
    private static final String TAG = "Matching View Activity";
    View v;
    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<MatchingProfile> profiles;
    private PetProfile profile;
    private EndlessRecyclerViewScrollListener scrollListener;
    private GridLayoutManager gridLayoutManager;
    private MatchingProfileDownloader md;
    private BestMatchFragment self;
    private String petId = "";
    public BestMatchFragment(){
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        v = inflater.inflate(R.layout.fragment_best_match, container, false);
        recyclerView = v.findViewById(R.id.bestMatchRecycle);
        self = this;
        profiles = new ArrayList<>();
        return v;

    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        petId = getActivity().getIntent().getStringExtra("petId");
        profile = new PetProfile();
        profile.download(petId, ()->{
            // Make sure profile is downloaded before requesting profile
            md = new MatchingProfileDownloader(profile, DEFAULT_DOWNLOAD_LIMIT);
            setRecyclerView();
        });
    }
    private void setRecyclerView(){
        recycleViewAdapter = new RecycleViewAdapter(this.getContext(), profiles, petId);
        gridLayoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recycleViewAdapter);
        md.downloadMore(profiles, petId,this);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                md.downloadMore(profiles,petId, self);
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