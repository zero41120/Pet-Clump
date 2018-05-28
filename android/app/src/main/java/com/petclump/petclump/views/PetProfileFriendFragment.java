package com.petclump.petclump.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petclump.petclump.R;


public class PetProfileFriendFragment extends Fragment {
    private ViewPager friendpetprofile_viewPager;
    private View v;
    public PetProfileFriendFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v =   inflater.inflate(R.layout.fragment_pet_profile_friend, container, false);
        friendpetprofile_viewPager = v.findViewById(R.id.friendpetprofile_viewPager);
//        ImagePager imagePager = new ImagePager(getActivity().getExtras().getString("petId"),this);
//        friendpetprofile_viewPager.setAdapter(imagePager);
        return v;
    }




}
