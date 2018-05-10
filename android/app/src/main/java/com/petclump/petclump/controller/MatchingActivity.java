package com.petclump.petclump.controller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.petclump.petclump.MatchingTab;
import com.petclump.petclump.R;
import com.petclump.petclump.models.DownloadImageTask;
import com.petclump.petclump.models.PetProfile;

/*****
 * * *
 * * *
 * * *
 *****/
public class MatchingActivity extends AppCompatActivity {
    private Button vm_button_settings;
    private CircularImageView match_pet1, match_pet2, match_pet3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);
        vm_button_settings = findViewById(R.id.vm_button_settings);
        match_pet1 = findViewById(R.id.match_pet1);
        match_pet2 = findViewById(R.id.match_pet2);
        match_pet3 = findViewById(R.id.match_pet3);
        vm_button_settings.setOnClickListener(v -> {
            startActivity(new Intent(this, UserInfoActivity.class));
        });
    }
    @Override
    protected void onResume() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        super.onResume();
        Intent i = new Intent(this, MatchingTab.class);
        match_pet1.setOnClickListener(v->{
            i.putExtra("petId", uid + "0");
            startActivity(i);
        });
        match_pet2.setOnClickListener(v->{
            i.putExtra("petId", uid + "1");
            startActivity(i);
        });
        match_pet3.setOnClickListener(v->{
            i.putExtra("petId", uid + "2");
            startActivity(i);
        });

        initializePrimaryPet();
    }
    private void initializePrimaryPet(){
        PetProfile thePet = new PetProfile();
        FirebaseAuth user = FirebaseAuth.getInstance();
        //profile_pet1
        thePet.download(user.getUid()+0,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(match_pet1).execute(url);
            //Toast.makeText(this, "1 set up", Toast.LENGTH_SHORT).show();
        });
        //profile_pet2
        thePet.download(user.getUid()+1,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(match_pet2).execute(url);
            //Toast.makeText(this, "2 set up", Toast.LENGTH_SHORT).show();
        });
        //profile_pet3
        thePet.download(user.getUid()+2,()->{
            String url = thePet.getUrl("main_profile_url");
            new DownloadImageTask(match_pet3).execute(url);
            //Toast.makeText(this, "3 set up", Toast.LENGTH_SHORT).show();
        });
    }
}
