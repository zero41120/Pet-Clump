package com.petclump.petclump;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MatchingViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_view);

        RecyclerView recyclerView = findViewById(R.id.matchviewRecycle);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, users);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(recycleViewAdapter);
    }


}
