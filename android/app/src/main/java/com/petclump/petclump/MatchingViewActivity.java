package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.petclump.petclump.models.PetProfile;

import java.util.ArrayList;
import java.util.List;

public class MatchingViewActivity extends AppCompatActivity {
    private List<PetProfile> pets;
    private static final String TAG = "MatchingViewActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);

        pets = new ArrayList<>();
        PetProfile gg = new PetProfile();
        gg.setAge("10 years old");
        gg.setBio("gg in the house");
        gg.setName("gg");

        PetProfile pp = new PetProfile();
        pp.setAge("IDK");
        pp.setBio("pp in the house");
        pp.setName("pp");

        PetProfile tt = new PetProfile();
        tt.setAge("RIP");
        tt.setBio("tt in the house");
        tt.setName("tt");
        pets.add(gg);
        pets.add(pp);
        pets.add(tt);
        Log.d(TAG, "onCreate: " + pets.size());

        RecyclerView recyclerView = findViewById(R.id.matchviewRecycle);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, pets);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recycleViewAdapter);
    }


}
