package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularimageview.CircularImageView;

public class UserInfoActivity extends AppCompatActivity {
    private int number_of_pets = 1;
    ImageButton button_add_pet;
    CircularImageView profile_pet1, profile_pet2, profile_pet3;
    TextView name_pet1, name_pet2, name_pet3;
    TextView user_name, user_gender, user_dob, user_match_range;
    Button edit_button;
    Context c;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        c = getApplicationContext();

        constraintSet = new ConstraintSet();
        constraintLayout = findViewById(R.id.user_info_layout);
        constraintSet.clone(constraintLayout);

        profile_pet1 = findViewById(R.id.profile_pet1);
        profile_pet2 = findViewById(R.id.profile_pet2);
        profile_pet3 = findViewById(R.id.profile_pet3);


        user_name = findViewById(R.id.user_name);
        user_gender = findViewById(R.id.user_gender);
        user_dob = findViewById(R.id.user_dob);
        user_match_range = findViewById(R.id.user_match_range);
        edit_button = findViewById(R.id.edit_button);

        if (2 == number_of_pets) {
            profile_pet2.setVisibility(View.VISIBLE);
            //name_pet2.setVisibility(View.VISIBLE);
        } else if (3 == number_of_pets) {
            profile_pet3.setVisibility(View.VISIBLE);
            profile_pet3.setVisibility(View.VISIBLE);
            //name_pet2.setVisibility(View.VISIBLE);
            //name_pet3.setVisibility(View.VISIBLE);
        }
        profile_pet1.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, PetInfoActivity.class);
            startActivity(nextScreen);
        });

        button_add_pet = findViewById(R.id.button_add_pet);
        button_add_pet.setOnClickListener(v -> add_pets());

        edit_button.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UserInfoEditActivity.class);
            startActivity(nextScreen);
        });
    }
    private void add_pets() {
        if (1 == number_of_pets) {
            number_of_pets += 1;
            profile_pet2.setVisibility(View.VISIBLE);
            //name_pet2.setVisibility(View.VISIBLE);
//
        } else if (2 == number_of_pets) {
            number_of_pets += 1;
            profile_pet3.setVisibility(View.VISIBLE);
            //name_pet3.setVisibility(View.VISIBLE);

        } else {//if 3==number
            Toast toast = Toast.makeText(c, "you've reached the maximum pet number!", Toast.LENGTH_LONG);
            toast.show();
        }

    }


}

