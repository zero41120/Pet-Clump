package com.petclump.petclump;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.petclump.petclump.models.OwnerProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.Specie;

import java.util.GregorianCalendar;

public class PetInfoEditActivity extends AppCompatActivity {
    private static final String TAG = "PetInfoEdit Activity";

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
        downloadData();
    }
    private void setupUI(){
        setContentView(R.layout.activity_pet_info_view);
        c = getApplicationContext();
        saveData();

    }
    private void downloadData(){

    }
    private void saveData() {
        PetProfile profile = new PetProfile();
        profile.setName("123");
        profile.setBio("i am from earth");
        profile.setAge("89");
        profile.setSpe(Specie.CAT.getName(c));

        profile.upload(user.getUid(), c);
    }
}
