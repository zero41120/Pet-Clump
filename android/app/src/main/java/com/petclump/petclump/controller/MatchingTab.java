package com.petclump.petclump.controller;

import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.petclump.petclump.R;
import com.petclump.petclump.views.BestMatchFragment;
import com.petclump.petclump.views.FriendFragment;
import com.petclump.petclump.views.ViewPagerAdapter;

public class MatchingTab extends AppCompatActivity {

    private ViewPager ViewPager_inTab;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_tab);
        tabLayout = findViewById(R.id.tabLayout);
        ViewPager_inTab = findViewById(R.id.ViewPager_inTab);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BestMatchFragment(), getResources().getString(R.string.Discovery));
        viewPagerAdapter.addFragment(new FriendFragment(),  getResources().getString(R.string.Friends));

        ViewPager_inTab.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(ViewPager_inTab);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setActionBar("Welcome");

    }
    public void setActionBar(String heading) {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_layout);
        actionBar.setDisplayHomeAsUpEnabled(true);
        TextView myText = findViewById(R.id.mytext);
        myText.setText(heading);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
        }

        return true;
    }
}


