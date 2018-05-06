package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MatchingViewProfileActivity extends AppCompatActivity {
    private TextView matchviewprofile_name, matchviewprofile_age, matchviewprofile_specie,
            matchviewprofile_bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view_profile);

        matchviewprofile_name = findViewById(R.id.matchviewprofile_name);
        matchviewprofile_age = findViewById(R.id.matchviewprofile_age);
        matchviewprofile_bio = findViewById(R.id.matchviewprofile_bio);
        matchviewprofile_specie = findViewById(R.id.matchviewprofile_specie);
    }
}
