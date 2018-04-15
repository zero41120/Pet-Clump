package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    ImageView user_profile;
    Button button_add_pets;
    ImageButton pets_profile_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        Context c = getApplicationContext();

        user_profile = findViewById(R.id.user_profile);

        pets_profile_view = findViewById(R.id.user_pet1);
        pets_profile_view.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, PetInfoActivity.class);
            startActivity(nextScreen);
        });

        button_add_pets = findViewById(R.id.button_add_pet);
        button_add_pets.setOnClickListener(v -> add_pets());
    }
    private void add_pets(){

    }

}
