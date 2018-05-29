package com.petclump.petclump.views;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.R;
import com.petclump.petclump.controller.FriendProfileActivity;
import com.petclump.petclump.models.FreeSchedule;
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
    private PetProfile petProfile, myProfile;
    private OwnerProfile ownerProfile, mySelf;
    private String owner_id, my_owner_id;
    private TextView friendownerprofile_name, friendownerprofile_dob, friendownerprofile_gender;
    private String TAG = "OwnerProfileFriendFrag";
    private FreeSchedule mySchedule, friendSchedule;
    private Activity activity;
    public OwnerProfileFriendFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v =   inflater.inflate(R.layout.fragment_owner_profile_friend, container, false);
        Activity activity = getActivity();
            friendownerprofile_viewPager = v.findViewById(R.id.friendownerprofile_viewPager);
            String friend_id = ((FriendProfileActivity)getActivity()).getIntent().getExtras().getString("friend_id");
            String my_id = ((FriendProfileActivity)getActivity()).getIntent().getExtras().getString("my_id");
            //Log.d(TAG, friend_id);
            friendownerprofile_name = v.findViewById(R.id.friendownerprofile_name);
            friendownerprofile_dob = v.findViewById(R.id.friendownerprofile_dob);
            friendownerprofile_gender= v.findViewById(R.id.friendownerprofile_gender);
            calendar = new GregorianCalendar();
            petProfile = new PetProfile();
            myProfile = new PetProfile();
            ownerProfile = OwnerProfile.getInstance();
            mySelf = OwnerProfile.getInstance();
            petProfile.download(friend_id, ()->{
                owner_id = petProfile.getOwnerId();
                ownerProfile.download(owner_id, ()->{
                    Log.d(TAG, owner_id);
                    friendSchedule = ownerProfile.getFreeTime();
                    calendar.setTime((Date) ownerProfile.getBirthday());
                    String t = String.valueOf(OwnerProfile.num_month(calendar.get(Calendar.MONTH)+1))+" "
                            +String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))+" "
                            +String.valueOf(calendar.get(Calendar.YEAR));
                    friendownerprofile_dob.setText(t);
                    friendownerprofile_gender.setText(ownerProfile.getGender());
                    friendownerprofile_name.setText(ownerProfile.getName());
                });
            });
            myProfile.download(my_id, ()->{
                my_owner_id = myProfile.getOwnerId();
                mySelf.download(my_owner_id, ()-> {
                    mySchedule = mySelf.getFreeTime();
                    if (isAdded() && activity!=null)
                        mutualSchedule(mySchedule, friendSchedule);
                    else
                        Log.d(TAG, "can't find Activity");
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



    public void mutualSchedule(FreeSchedule mine, FreeSchedule friend){
        for (int i=1; i<8; i++){
            for (int j=1; j<4; j++){
                String imageID = "ms" + i + j;
                int resID = getResources().getIdentifier(imageID, "id", "com.petclump.petclump");
                Log.d(TAG, getActivity().getPackageName());
                ImageView im = v.findViewById(resID);
                if(mine.isFree(i,j) && friend.isFree(i, j)){
                    im.setTag(R.drawable.schedule_green);
                    im.setImageResource(R.drawable.schedule_green);
                }else{
                    im.setTag(R.drawable.schedule_gray);
                }
            }
        }
    }

    @Override
    public void didCompleteDownload() {

    }
}
