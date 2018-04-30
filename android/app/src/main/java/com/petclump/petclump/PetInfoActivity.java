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

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;


public class PetInfoActivity extends AppCompatActivity {
    private static final String TAG = "Pet Info Activity";


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;


    Button Button_to_quiz, Button_return;
    ImageButton Button_edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUI();


    }
    private void setupUI(){
        setContentView(R.layout.activity_pet_info);

        Button_to_quiz = findViewById(R.id.Button_to_quiz);
        Button_return = findViewById(R.id.Button_return);
        Button_edit = findViewById(R.id.Button_edit);
        Button_to_quiz.setOnClickListener(v ->
                startActivity(new Intent(this, QuizActivity.class))
        );
        Button_return.setOnClickListener(v ->
                finish()
        );
        Button_edit.setOnClickListener(v->
                startActivity(new Intent(this,PetInfoEditActivity.class))
        );

    }
}
