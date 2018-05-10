package com.petclump.petclump;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.petclump.petclump.views.BestMatchFragment;
import com.petclump.petclump.views.FriendFragment;
import com.petclump.petclump.views.LocalFragment;
import com.petclump.petclump.views.ViewPagerAdapter;

public class MatchingTab extends AppCompatActivity {

    private ViewPager ViewPager_inTab;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_tab);

        tabLayout = findViewById(R.id.tabLayout);
        ViewPager_inTab = findViewById(R.id.ViewPager_inTab);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BestMatchFragment(), getResources().getString(R.string.Best_match));
        viewPagerAdapter.addFragment(new LocalFragment(), getResources().getString(R.string.Local));
        viewPagerAdapter.addFragment(new FriendFragment(),  getResources().getString(R.string.Friends));


        ViewPager_inTab.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(ViewPager_inTab);

    }
}


