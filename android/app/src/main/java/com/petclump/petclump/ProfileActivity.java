package com.petclump.petclump;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    TextView textName, textEmail;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        textName = findViewById(R.id.textViewName);
        textEmail = findViewById(R.id.textViewEmail);

        FirebaseUser user = mAuth.getCurrentUser();

        textName.setText(user.getDisplayName());
        textEmail.setText(user.getEmail());

    }
    @Override
    protected void onStart(){

        super.onStart();

        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
