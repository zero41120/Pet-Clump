package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.petclump.petclump.models.Specie;

public class MainActivity extends AppCompatActivity {

    Button pickButton, settingsButton;
    TextView hello;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c = getApplicationContext();

        pickButton = findViewById(R.id.button_go_upload_photo);
        pickButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UploadPhotoActivity.class);
            startActivity(nextScreen);
        });

        settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UserInfoActivity.class);
            startActivity(nextScreen);
        });

        hello = findViewById(R.id.hello);


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        for (Specie s: Specie.values()) {
                            Thread.sleep(1000);
                            runOnUiThread( ()-> hello.setText(s.getName(c)));
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d("Interrupted", "run: " + e.getMessage());
                }
            }
        };
        t.start();

    }
}
