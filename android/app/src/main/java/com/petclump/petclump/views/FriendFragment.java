package com.petclump.petclump.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.petclump.petclump.R;
import com.petclump.petclump.models.FriendListDownloader;
import com.petclump.petclump.models.FriendProfile;
import com.petclump.petclump.models.PetProfile;
import com.petclump.petclump.models.protocols.FriendChangeState;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Nullable;

public class FriendFragment extends Fragment implements ProfileDownloader {
    View v;
    private static String TAG = "FriendFragment";
    private RecyclerView recyclerView;
    private FriendRecycleViewAdapter friendRecycleViewAdapter;
    private List<FriendProfile> friendProfileList;
    private LinearLayoutManager linearLayoutManager;
    private PetProfile pet = new PetProfile();
    private String pet_id = "";
    private Map<String,String> Friend_list = null;
    private FriendListDownloader downloader;
    public FriendFragment(){}
    private ArrayList<String> download_list;

    @Override
    public void didCompleteDownload() {
        friendProfileList.add(new FriendProfile(pet.getName(), "what's up", "13:00", pet.getPhotoUrl(PetProfile.UrlKey.main)));
        friendRecycleViewAdapter.notifyDataSetChanged();
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView = v.findViewById(R.id.friendview_recycle);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        friendProfileList = new ArrayList<>();

       /* FriendProfile profile1 = new FriendProfile("Wang Chai", "Hey what's up", "13:09");
        FriendProfile profile2 = new FriendProfile("Funk", "Wanna go for walk?", "16:09");
        FriendProfile profile3 = new FriendProfile("Nei Nei", "Yo Yo", "2:09");
        FriendProfile profile4 = new FriendProfile("Sueanne Li", "Yo Yo", "8:19");
        FriendProfile profile5 = new FriendProfile("Shay Wang", "想吃花椰菜", "12:09");
        FriendProfile profile6 = new FriendProfile("Wendy Wang", "......", "1:09");

        friendProfileList.add(profile4);
        friendProfileList.add(profile5);
        friendProfileList.add(profile6);*/
        //Log.d(TAG,"要他妈的return啦");
        return v;
    }
    private void setRecyclerView(){
        friendRecycleViewAdapter = new FriendRecycleViewAdapter(this.getContext(), friendProfileList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(friendRecycleViewAdapter);
        recyclerView.invalidate();
    }
    private void unread_friend(String sender_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        pet.download(sender_id,()->{
            builder
                    .setTitle("New Friend Request")
                    .setMessage("You have received a new Request from " + pet.getName() + " , will you accept it?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", (DialogInterface dialog, int which) -> {
                        pet.new_friend_change(sender_id, pet_id, PetProfile.friend_change_type.ADD_UNREAD_FRIEND, ()->{
                            Toast.makeText(getActivity(), "Successfully add the friend!", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .setNegativeButton("Block", (DialogInterface dialog, int which) -> {
                        pet.new_friend_change(sender_id, pet_id, PetProfile.friend_change_type.BLOCK_FRIEND, ()->{
                            Toast.makeText(getActivity(), "Successfully block the request!", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .setNeutralButton("Refuse", (DialogInterface dialog, int which) -> {
                        pet.friend_delete(sender_id, pet_id, ()->{
                            Toast.makeText(getActivity(), "Successfully refuse the request!", Toast.LENGTH_SHORT).show();
                        });
                    } ).show();
        });
    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pet_id = getActivity().getIntent().getStringExtra("petId");
        pet.listenToFriendList(pet_id,()->{
            Friend_list = (Map<String, String>) pet.getRelation_list().clone();
            Log.d(TAG, "didCompleteDownload: " + Friend_list);
            //download_list = new ArrayList<>();
            for (Map.Entry<String,String> entry : Friend_list.entrySet()){
                // friend_list
                if(entry.getValue().equals("1")){
                    //download_list.add(entry.getKey());
                    pet.download(entry.getKey(), this);
                }
                // unread friend request list
                if(entry.getValue().equals("0")){
                    unread_friend(entry.getKey());
                }
            }
            setRecyclerView();
        });// download friend_list
    }
}