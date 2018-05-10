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
    View v;
    private RecyclerView recyclerView;
    private List<MatchingProfile> pets;
    private PetProfile profile;
    private GridLayoutManager gridLayoutManager;
    private MatchingProfileDownloader md;
    public BestMatchFragment(){
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        v = inflater.inflate(R.layout.fragment_best_match, container, false);
        recyclerView = v.findViewById(R.id.bestMatchRecycle);
//        String petId = this.getActivity().getIntent().getStringExtra("petId");
//        pets = new ArrayList<>();
//        profile = new PetProfile();
//        profile.download(petId, ()->{
//            // Make sure profile is downloaded before requesting profile
//            md = new MatchingProfileDownloader(profile, 30);
//            md.downloadMore(pets, this);
//            RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getContext(), pets);
//            gridLayoutManager = new GridLayoutManager(getContext(), 2);
//            recyclerView.setLayoutManager(gridLayoutManager);
//            recyclerView.setAdapter(recycleViewAdapter);
//
//        });

        return super.onCreateView(inflater, container, saveInstanceState);

    }


    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void didCompleteDownload() {
        Log.d("hey", "didCompleteDownload: "+ pets);

    }
}