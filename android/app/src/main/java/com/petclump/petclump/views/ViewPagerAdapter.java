package com.petclump.petclump.views;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> listFragment = new ArrayList<>();
    private final List<String> fragTitle = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitle.get(position);
    }

    @Override
    public int getCount() {
        return fragTitle.size();
    }

    public void addFragment(Fragment fragment, String title){
        listFragment.add(fragment);
        fragTitle.add(title);
    }


}
