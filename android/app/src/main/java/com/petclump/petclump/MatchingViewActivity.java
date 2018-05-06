package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.petclump.petclump.models.PetProfile;

import java.util.ArrayList;
import java.util.List;

public class MatchingViewActivity extends AppCompatActivity {
    private List<PetProfile> pets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);

        pets = new ArrayList<>();
        pets.add(new PetProfile());

        RecyclerView recyclerView = findViewById(R.id.matchviewRecycle);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, pets);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recycleViewAdapter);
    }


}
