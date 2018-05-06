package com.petclump.petclump;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;

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
        Intent i = new Intent(this, MatchingViewActivity.class);
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
    }
}
