package com.petclump.petclump.controller;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.petclump.petclump.R;
import com.petclump.petclump.views.ImagePager;
import com.petclump.petclump.views.OwnerProfileFriendFragment;
import com.petclump.petclump.views.PetProfileFriendFragment;
import com.petclump.petclump.views.ViewPagerAdapter;

public class FriendProfileActivity extends AppCompatActivity {

    private ViewPager ViewPager_inTab_profile;
    private TabLayout tabLayout2;
    private ViewPagerAdapter viewPagerAdapter;
    private String TAG = "FriendprofileActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Intent intent = getIntent();
        String friend_id = intent.getExtras().getString("friend_id");
        Log.d(TAG, friend_id); 
        tabLayout2 = findViewById(R.id.tabLayout2);
        ViewPager_inTab_profile = findViewById(R.id.ViewPager_inTab_profile);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new PetProfileFriendFragment(), getResources().getString(R.string.pet));
        viewPagerAdapter.addFragment(new OwnerProfileFriendFragment(), getResources().getString(R.string.owner));



        ViewPager_inTab_profile.setAdapter(viewPagerAdapter);
        tabLayout2.setupWithViewPager(ViewPager_inTab_profile);

    }
}
