package com.petclump.petclump.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.views.ImagePager;

import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
// Circular indicator thanks to this github library
// https://github.com/ongakuer/CircleIndicator
public class MatchingViewProfileActivity extends AppCompatActivity implements ProfileDownloader {
    private TextView matchviewprofile_name, matchviewprofile_age, matchviewprofile_specie,
            matchviewprofile_bio;
    private ViewPager matchviewprofile_viewPager;
    private Button matchviewprofile_add_friend, report_btn;
    private PetProfile petProfile;
    private String Name = "";
    private static final int ITEM1 = Menu.FIRST;

    private static final int ITEM2 = Menu.FIRST + 1;

    private static final int ITEM3 = Menu.FIRST + 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view_profile);
        Intent intent = getIntent();
        matchviewprofile_name = findViewById(R.id.matchviewprofile_name);
        matchviewprofile_age = findViewById(R.id.matchviewprofile_age);
        matchviewprofile_bio = findViewById(R.id.matchviewprofile_bio);
        matchviewprofile_specie = findViewById(R.id.matchviewprofile_specie);
        matchviewprofile_add_friend = findViewById(R.id.button_add_friend);
        report_btn = findViewById(R.id.button_report);
        report_btn.setOnClickListener(v->{
            report_btn.setBackgroundResource(R.drawable.cancel_button_background);
            report_btn.setText("Reported");
        });
        registerForContextMenu(report_btn);
        String main_pet_id = intent.getExtras().getString("MainPetId");
        String the_pet_id = intent.getExtras().getString("petId");
        matchviewprofile_viewPager = findViewById(R.id.matchviewprofile_viewPager);
        petProfile = new PetProfile();
        petProfile.download(the_pet_id, ()->{
           matchviewprofile_bio.setText(petProfile.getBio());
            matchviewprofile_specie.setText(petProfile.getSpe());
            matchviewprofile_name.setText(petProfile.getName());
            matchviewprofile_age.setText(petProfile.getAge());
            Name = petProfile.getName();
            setActionBar(Name);
        });
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
        PetProfile pet = new PetProfile();

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
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        menu.add(0, ITEM1, 0, "Profanity");
        menu.add(0, ITEM2, 0, "Too mean");
        menu.add(0, ITEM3, 0, "Inappropriate photos");

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case ITEM1:
                Toast.makeText(this, "inappropriate language ", Toast.LENGTH_SHORT).show();
                break;
            case ITEM2:
                Toast.makeText(this, "太兇", Toast.LENGTH_SHORT).show();
                break;
            case ITEM3:
                Toast.makeText(this, "inappropriate photos", Toast.LENGTH_SHORT).show();
                break;

        }
        return true;
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

    @Override
    public void didCompleteDownload() {

    }
}
