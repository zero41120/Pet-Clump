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
import android.widget.Toast;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity {
    ImageView user_profile;
    ImageButton button_add_pets, button_edit_user_photo;
    ImageView pet_1;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        c = getApplicationContext();

        user_profile = findViewById(R.id.user_profile);

        pet_1 = findViewById(R.id.user_pet1);
        pet_1.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, PetInfoActivity.class);
            startActivity(nextScreen);
        });

        button_add_pets = findViewById(R.id.button_add_pet);
        button_add_pets.setOnClickListener(v -> add_pets());

        button_edit_user_photo = findViewById(R.id.button_edit_user_photo);
        button_edit_user_photo.setOnClickListener(v -> edit_photo());
    }
    private void add_pets(){
        Toast toast = Toast.makeText(c, "add pets!", Toast.LENGTH_LONG);
        toast.show();
    }
    private void edit_photo(){
        Toast toast = Toast.makeText(c, "edit photo!", Toast.LENGTH_LONG);
        toast.show();
    }

}
