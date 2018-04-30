package com.petclump.petclump;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class PetInfoActivity extends AppCompatActivity{
    private static final String TAG = "Pet Info Activity";
    private TextView pet_name, pet_spe, pet_age, pet_bio;
    private int sequence = -1;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;

    Button Button_to_quiz, Button_return, Button_save;
    ImageButton Button_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_info);
        Bundle extras = getIntent().getExtras();
        sequence = (Integer) extras.get("sequence");
        Log.d(TAG,"sequence:"+sequence);


        setupUI();

    }
    private void setupUI(){

        Button_to_quiz = findViewById(R.id.Button_to_quiz);
        Button_return = findViewById(R.id.Button_return);
        Button_edit = findViewById(R.id.Button_edit);
        Button_save = findViewById(R.id.button_save);

        // set up UI
        pet_age = findViewById(R.id.pet_name);
        pet_bio = findViewById(R.id.pet_bio);
        pet_spe = findViewById(R.id.pet_specie);
        pet_name = findViewById(R.id.pet_name);

        PetProfile profile = new PetProfile();
        profile.download(user.getUid()+sequence, () -> {
            pet_age.setText(profile.getAge());
            pet_bio.setText(profile.getBio());
            pet_spe.setText(profile.getSpe());
            pet_name.setText(profile.getName());
        });

        Button_to_quiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizActivity.class))
        );
        Button_return.setOnClickListener(v ->
                finish()
        );
        Button_edit.setOnClickListener(v->{
            Intent i = new Intent(this, PetInfoEditActivity.class);
            i.putExtra("sequence", sequence);
            startActivity(i);
        });

        Button_save.setOnClickListener(v->{
            PetProfile pet = new PetProfile();
            pet.upload(user.getUid()+sequence,()->{
                pet.setBio(pet_bio.getText().toString());
                pet.setAge(pet_age.getText().toString());
                pet.setName(pet_name.getText().toString());
                pet.setOwner_id(user.getUid());
                pet.setSequence(sequence);
            });
        });
    }



}
