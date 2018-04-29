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

public class PetInfoActivity extends AppCompatActivity {
    private static final String TAG = "Pet Info Activity";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(user == null){
            Log.d(TAG, "onCreate: User not logged in");
            finish();
        }
        setupUI();
        //downloadData();
    }
    private void setupUI(){
        setContentView(R.layout.activity_pet_info);
        c = getApplicationContext();
        Button b = (Button)findViewById(R.id.button2);
        b.setOnClickListener(v -> startActivity(new Intent(c, PetInfoEditActivity.class)));
    }
    private void downloadData(){

    }
}
