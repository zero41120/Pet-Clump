package com.petclump.petclump.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.petclump.petclump.R;
import com.petclump.petclump.controller.FriendProfileActivity;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.models.protocols.ProfileUploader;

import org.w3c.dom.Text;


public class PetProfileFriendFragment extends Fragment implements ProfileDownloader{
    private ViewPager friendpetprofile_viewPager;
    private View v;
    private FriendProfileActivity friendProfileActivity;
    private PetProfile petProfile;
    private TextView friendpetprofile_bio, friendpetprofile_age, friendpetprofile_specie, friendpetprofile_name;
    private String TAG = "PetProfileFriendFrag";

    public PetProfileFriendFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v =   inflater.inflate(R.layout.fragment_pet_profile_friend, container, false);
        friendpetprofile_viewPager = v.findViewById(R.id.friendpetprofile_viewPager);
        String friend_id = ((FriendProfileActivity)getActivity()).getIntent().getExtras().getString("friend_id");
        //Log.d(TAG, friend_id);
        friendpetprofile_bio = v.findViewById(R.id.friendpetprofile_bio);
        friendpetprofile_specie = v.findViewById(R.id.friendpetprofile_specie);
        friendpetprofile_name= v.findViewById(R.id.friendpetprofile_name);
        friendpetprofile_age = v.findViewById(R.id.friendpetprofile_age);
        petProfile = new PetProfile();
        petProfile.download(friend_id, ()->{
            friendpetprofile_bio.setText(petProfile.getBio());
            friendpetprofile_specie.setText(petProfile.getSpe());
            friendpetprofile_name.setText(petProfile.getName());
            friendpetprofile_age.setText(petProfile.getAge());
        });
        ImagePager imagePager = new ImagePager(friend_id, getActivity() );
        friendpetprofile_viewPager.setAdapter(imagePager);
        return v;
    }


    @Override
    public void didCompleteDownload() {

    }
}
