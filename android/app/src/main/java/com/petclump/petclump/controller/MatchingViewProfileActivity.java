package com.petclump.petclump.controller;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.views.ImagePager;

import me.relex.circleindicator.CircleIndicator;
// Circular indicator thanks to this github library
// https://github.com/ongakuer/CircleIndicator
public class MatchingViewProfileActivity extends AppCompatActivity {
    private TextView matchviewprofile_name, matchviewprofile_age, matchviewprofile_specie,
            matchviewprofile_bio;
    private ViewPager matchviewprofile_viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view_profile);
        Intent intent = getIntent();
        String Name = intent.getExtras().getString("Name");
        matchviewprofile_name = findViewById(R.id.matchviewprofile_name);
        matchviewprofile_name.setText(Name);
        String Age = intent.getExtras().getString("Age");
        matchviewprofile_age = findViewById(R.id.matchviewprofile_age);
        matchviewprofile_age.setText(Age);
        String Bio = intent.getExtras().getString("Bio");
        matchviewprofile_bio = findViewById(R.id.matchviewprofile_bio);
        matchviewprofile_bio.setText(Bio);
        matchviewprofile_specie = findViewById(R.id.matchviewprofile_specie);
        String Spe = intent.getExtras().getString("Spe");
        matchviewprofile_specie.setText(Spe);

        matchviewprofile_viewPager = findViewById(R.id.matchviewprofile_viewPager);
        ImagePager imagePager = new ImagePager(intent.getExtras().getString("petId"),this);
        matchviewprofile_viewPager.setAdapter(imagePager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(matchviewprofile_viewPager);
        //new DownloadImageTask(matchviewprofile_viewPager).execute(intent.getExtras().getString("main_url"));
    }
}
