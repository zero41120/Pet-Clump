package com.petclump.petclump.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.views.ImagePager;

import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
// Circular indicator thanks to this github library
// https://github.com/ongakuer/CircleIndicator
public class MatchingViewProfileActivity extends AppCompatActivity {
    private TextView matchviewprofile_name, matchviewprofile_age, matchviewprofile_specie,
            matchviewprofile_bio;
    private ViewPager matchviewprofile_viewPager;
    private Button matchviewprofile_add_friend;
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
        matchviewprofile_add_friend = findViewById(R.id.button_add_friend);

        matchviewprofile_viewPager = findViewById(R.id.matchviewprofile_viewPager);
        String[] MatchImage= new String[]{
                "main_profile_url",
                "pet_profile_url_1",
                "pet_profile_url_2",
                "pet_profile_url_3",
                "pet_profile_url_4",
                "pet_profile_url_5",
        };
        ImagePager imagePager = new ImagePager(intent.getExtras().getString("petId"),this, MatchImage);
        matchviewprofile_viewPager.setAdapter(imagePager);
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(matchviewprofile_viewPager);
        //new DownloadImageTask(matchviewprofile_viewPager).execute(intent.getExtras().getString("main_url"));
        String main_pet_id = intent.getExtras().getString("MainPetId");
        String the_pet_id = intent.getExtras().getString("petId");
        PetProfile pet = new PetProfile();
        setActionBar(Name);
        Context temp = this;
        matchviewprofile_add_friend.setOnClickListener(v-> {
            pet.new_friend_change(main_pet_id, the_pet_id, PetProfile.friend_change_type.NEW_FRIEND,temp, () -> {
                Toast.makeText(this, "send successfully!", Toast.LENGTH_SHORT).show();
                matchviewprofile_add_friend.setClickable(false);
                matchviewprofile_add_friend.setText("Already sent");
                matchviewprofile_add_friend.setBackgroundResource(R.drawable.cancel_button_background);
            });
        });
    }
    public void setActionBar(String heading) {
        // TODO Auto-generated method stub

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        TextView myText = findViewById(R.id.mytext);
        actionBar.setDisplayHomeAsUpEnabled(true);
        myText.setText(heading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
        }

        return true;
    }
}
