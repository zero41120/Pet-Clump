package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {
    ImageView user_profile_view, pet_profile_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        user_profile_view = findViewById(R.id.user_profile);
        pet_profile_view = findViewById(R.id.pet_profile);

    }

}
