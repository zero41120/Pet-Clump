package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.UserInfo;
import com.mikhaellopez.circularimageview.CircularImageView;

public class UserInfoActivity extends AppCompatActivity {
    private int number_of_pets = 1;
    ImageView profile_user;
    ImageButton button_add_pets, button_edit_user_photo;
    CircularImageView profile_pet1, profile_pet2, profile_pet3;
    TextView name_pet1, name_pet2, name_pet3;
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


        profile_user = findViewById(R.id.profile_user);
        profile_pet1 = findViewById(R.id.profile_pet1);
        profile_pet2 = findViewById(R.id.profile_pet2);
        profile_pet3 = findViewById(R.id.profile_pet3);
        name_pet1 = findViewById(R.id.name_pet1);
        name_pet2 = findViewById(R.id.name_pet2);
        name_pet3 = findViewById(R.id.name_pet3);
        if (2==number_of_pets){
            profile_pet2.setVisibility(View.VISIBLE);
            name_pet2.setVisibility(View.VISIBLE);
        }else if (3==number_of_pets){
            profile_pet3.setVisibility(View.VISIBLE);
            profile_pet3.setVisibility(View.VISIBLE);
            name_pet2.setVisibility(View.VISIBLE);
            name_pet3.setVisibility(View.VISIBLE);
        }
        profile_pet1.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, PetInfoActivity.class);
            startActivity(nextScreen);
        });

        button_add_pets = findViewById(R.id.button_add_pet);
        button_add_pets.setOnClickListener(v -> add_pets());

        button_edit_user_photo = findViewById(R.id.button_edit_user_photo);
        button_edit_user_photo.setOnClickListener(v -> edit_photo());
    }

    private void add_pets() {
        if (1 == number_of_pets) {
              number_of_pets+=1;
              profile_pet2.setVisibility(View.VISIBLE);
              name_pet2.setVisibility(View.VISIBLE);
//
        }else if (2 == number_of_pets) {
            profile_pet3.setVisibility(View.VISIBLE);
            name_pet3.setVisibility(View.VISIBLE);

        }else {//if 3==number
            Toast toast = Toast.makeText(c, "you've reached the maximum pet number!", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void edit_photo() {
        Toast toast = Toast.makeText(c, "edit photo!", Toast.LENGTH_LONG);
        toast.show();
    }
//    public void TextViewClicked(View view) {
//        ViewSwitcher switcher = (ViewSwitcher) findViewById(R.id.my_switcher);
//        switcher.showNext(); //or switcher.showPrevious();
//        TextView myTV = switcher.findViewById(R.id.clickable_text_view);
//        EditText myTX = switcher.findViewById(R.id.hidden_edit_view)
//        //myTV.setText(getString((EditText)myTX));
//        switcher.showPrevious();
//    }
}