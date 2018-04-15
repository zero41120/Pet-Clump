package com.petclump.petclump;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.petclump.petclump.models.Specie;

public class MainActivity extends AppCompatActivity {

    Button pickButton, settingsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pickButton = findViewById(R.id.button_go_upload_photo);
        pickButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), UploadPhotoActivity.class);
            startActivity(nextScreen);
        });

        settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), UserInfoActivity.class);
            startActivity(nextScreen);
        });

        ((TextView)findViewById(R.id.hello)).setText(Specie.DOG.getName(getApplicationContext()));

    }
}
