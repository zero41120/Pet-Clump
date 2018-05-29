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
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class OwnerProfileFriendFragment extends Fragment implements ProfileDownloader{
    private ViewPager friendownerprofile_viewPager;
    private View v;
    private Calendar calendar;
    private FriendProfileActivity friendProfileActivity;
    private PetProfile petProfile;
    private OwnerProfile ownerProfile;
    private String owner_id;
    private TextView friendownerprofile_name, friendownerprofile_dob, friendownerprofile_gender;
    private String TAG = "OwnerProfileFriendFrag";



    public OwnerProfileFriendFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v =   inflater.inflate(R.layout.fragment_owner_profile_friend, container, false);
        friendownerprofile_viewPager = v.findViewById(R.id.friendownerprofile_viewPager);
        String friend_id = ((FriendProfileActivity)getActivity()).getIntent().getExtras().getString("friend_id");
        //Log.d(TAG, friend_id);
        friendownerprofile_name = v.findViewById(R.id.friendownerprofile_name);
        friendownerprofile_dob = v.findViewById(R.id.friendownerprofile_dob);
        friendownerprofile_gender= v.findViewById(R.id.friendownerprofile_gender);
        calendar = new GregorianCalendar();
        petProfile = new PetProfile();
        ownerProfile = OwnerProfile.getInstance();
        petProfile.download(friend_id, ()->{
            owner_id = petProfile.getOwnerId();
            Log.d(TAG, owner_id );
            ownerProfile.download(owner_id, ()->{
                Log.d(TAG, owner_id);
                calendar.setTime((Date) ownerProfile.getBirthday());
                String t = String.valueOf(OwnerProfile.num_month(calendar.get(Calendar.MONTH)+1))+" "
                        +String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" "
                        +String.valueOf(calendar.get(Calendar.YEAR));
                Log.d(TAG, "t" + t);
                Log.d(TAG, "gender" + ownerProfile.getGender());
                friendownerprofile_dob.setText(t);
                friendownerprofile_gender.setText(ownerProfile.getGender());
                friendownerprofile_name.setText(ownerProfile.getName());
            });
        });
        String[] MatchImage= new String[]{
                "group_profile_url_1",
                "group_profile_url_2",
                "group_profile_url_3"
        };
        ImagePager imagePager = new ImagePager(friend_id, getActivity(), MatchImage );
        friendownerprofile_viewPager.setAdapter(imagePager);
        return v;
    }


    @Override
    public void didCompleteDownload() {

    }
}
