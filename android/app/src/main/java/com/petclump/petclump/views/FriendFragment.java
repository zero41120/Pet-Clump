package com.petclump.petclump.views;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.R;
import com.petclump.petclump.models.Cryptography.KeyExchanger;
import com.petclump.petclump.models.FriendProfile;
import com.petclump.petclump.models.MessagingDownloader;
import com.petclump.petclump.models.PetProfile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class FriendFragment extends Fragment {
    View v;
    private static String TAG = "FriendFragment";
    private RecyclerView recyclerView;
    private FriendRecycleViewAdapter friendRecycleViewAdapter;
    private List<FriendProfile> friendProfileList;
    private LinearLayoutManager linearLayoutManager;
    private PetProfile pet = new PetProfile();
    private String pet_id = "";
    private Map<String,String> Friend_list = null;
    public FriendFragment(){}


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState) {
        v = inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView = v.findViewById(R.id.friendview_recycle);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        friendProfileList = new ArrayList<>();
        return v;
    }
    private void setRecyclerView(){
        friendRecycleViewAdapter = new FriendRecycleViewAdapter(getContext(), friendProfileList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(friendRecycleViewAdapter);
        recyclerView.invalidate();
    }

    @Override
    public void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pet_id = getActivity().getIntent().getStringExtra("petId");

        pet.listenToFriendList(pet_id,()->{
            Friend_list = (Map<String, String>) pet.getRelation_list().clone();
            Log.d(TAG, "didCompleteDownload: " + Friend_list);
            friendProfileList.clear();

            for (Map.Entry<String,String> entry : Friend_list.entrySet()){

                if(!entry.getValue().equals("blocking") && !entry.getValue().equals("sending")){
                    // setup last message listener
                    FirebaseFirestore.getInstance().collection("chats").document(PetProfile.getCombinedId(pet_id, entry.getKey())).get().addOnCompleteListener(task->{
                        String friend_id = entry.getKey();
                        Map<String, Object> ref = task.getResult().getData();
                        BigInteger my_public = new BigInteger(ref.get(pet_id).toString());
                        BigInteger friend_public = new BigInteger(ref.get(friend_id).toString());
                        BigInteger bigPRIME = new BigInteger(ref.get("bigPrime").toString());
                        BigInteger priPRIME = new BigInteger(ref.get("priPrime").toString());
/*                        Log.d(TAG,"my_public:"+my_public);
                        Log.d(TAG,"friend_public:"+friend_public);
                        Log.d(TAG,"bigPRIME:"+bigPRIME);
                        Log.d(TAG,"priPRIME"+priPRIME);*/
                        try {
                            KeyExchanger my = new KeyExchanger(pet_id, friend_public, bigPRIME, priPRIME);
                            byte[] myShared = my.getSharedKey(friend_public);
                            String sharedString = "";
                            for(byte b : myShared){  sharedString += b; }
                            Log.d(TAG, "mySharedKey: " + sharedString);

                            pet.download(entry.getKey(), ()->{
                                FriendProfile t = new FriendProfile(pet_id, entry.getKey(), pet.getName(),
                                        "Added you", "13:00", pet.getPhotoUrl(PetProfile.UrlKey.main), entry.getValue());
                                // setup last messaging.
                                //if(!friendProfileList.contains(t)){
                                    MessagingDownloader.setlastMessage(pet_id,friend_id,myShared,t,()->{
                                        if(friendProfileList.contains(t)) {
                                            int index = friendProfileList.indexOf(t);
                                            friendProfileList.remove(index);
                                            friendProfileList.add(index,t);
                                            friendRecycleViewAdapter.notifyDataSetChanged();
                                        }else{
                                            friendProfileList.add(t);
                                            friendRecycleViewAdapter.notifyDataSetChanged();
                                        }
                                    });
                                //}
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                }
            }
            //friendRecycleViewAdapter.notifyDataSetChanged();
            setRecyclerView();
        });
    }
}