package com.petclump.petclump.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.petclump.petclump.R;
import com.petclump.petclump.models.FriendProfile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;


public class FriendFragment extends Fragment {
    View v;
    private static String TAG = "FriendFragment";
    private RecyclerView recyclerView;
    private FriendRecycleViewAdapter friendRecycleViewAdapter;
    private List<FriendProfile> friendProfileList;
    private LinearLayoutManager linearLayoutManager;
    public FriendFragment() {
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_friend, container, false);
        Log.d(TAG, "is this null too?" + v);
        recyclerView = v.findViewById(R.id.friendview_recycle);
        Log.d(TAG, "is this null too?" + recyclerView);
        friendProfileList = new ArrayList<>();


        FriendProfile profile1 = new FriendProfile("Wang Chai", "Hey what's up", "13:09");
        FriendProfile profile2 = new FriendProfile("Funk", "Wanna go for walk?", "16:09");
        FriendProfile profile3 = new FriendProfile("Nei Nei", "Yo Yo", "2:09");
        FriendProfile profile4 = new FriendProfile("Sueanne Li", "Yo Yo", "8:19");
        FriendProfile profile5 = new FriendProfile("Shay Wang", "想吃花椰菜", "12:09");
        FriendProfile profile6 = new FriendProfile("Wendy Wang", "......", "1:09");
        friendProfileList.add(profile1);
        friendProfileList.add(profile2);
        friendProfileList.add(profile3);
        friendProfileList.add(profile4);
        friendProfileList.add(profile5);
        friendProfileList.add(profile6);

        friendRecycleViewAdapter = new FriendRecycleViewAdapter(this.getContext(), friendProfileList);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        if (recyclerView==null){
            Log.d(TAG, "Why is this null????????");

        }
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(friendRecycleViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}