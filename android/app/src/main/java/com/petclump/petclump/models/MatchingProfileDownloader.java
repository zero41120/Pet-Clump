package com.petclump.petclump.models;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MatchingProfileDownloader {
    private static final String COLLECTION_NAME = "pets";
    private static final String TAG = "ProfileDownloader";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer downloadLimit = 2;
    private Query petProfileQuery;
    private PetProfile pet;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private OwnerProfile owner = OwnerProfile.getInstance();
    public MatchingProfileDownloader(PetProfile myPet, Integer downloadLimit){
        this.downloadLimit = downloadLimit;
        this.pet = myPet;
        petProfileQuery = db.collection(COLLECTION_NAME).limit(downloadLimit);

    }

    public void downloadMore(List<MatchingProfile> toAppend, String id, ProfileDownloader c){
        pet.listenToFriendList(id, ()->{
            Map<String, String> pet_list = (Map<String, String>) pet.getRelation_list().clone();
            //List<MatchingProfile> profiles = new ArrayList<>();
            ObservableArrayList<MatchingProfile> profiles = new ObservableArrayList<>();
            petProfileQuery.get().addOnSuccessListener(documentSnapshots -> {
                if(documentSnapshots.size() -1 < 0){ return; }
                DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                        .get(documentSnapshots.size() -1);
                List<DocumentSnapshot> valid_document = new ArrayList<>();
                for (DocumentSnapshot doc: documentSnapshots.getDocuments()) {
                    // if should block pet
                    if(user_pets(doc.getId()) || relation_pets(doc.getId(), pet_list)){
                        continue;
                    }
                    valid_document.add(doc);
                }
                for (DocumentSnapshot doc: valid_document) {
                    PetProfile petInfo = new PetProfile(doc.getData());
                    MatchingProfile match = new MatchingProfile(this.pet.getQuiz(), petInfo);
                    match.calculatePercent(()->{
                        profiles.add(match);
                    });
                }

                profiles.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList>(){
                    @Override
                    public void onChanged(ObservableList sender) {
                        Log.d("onCheanged",  sender.toString());
                    }
                    @Override
                    public void onItemRangeChanged(ObservableList sender, int positionStart, int itemCount) {
                        Log.d("onItemRangeChanged",  itemCount +"");
                    }
                    @Override
                    public void onItemRangeInserted(ObservableList sender, int positionStart, int itemCount) {
                        Log.d("onItemRangeInserted",  "size:"+profiles.size());
                        if(profiles.size() >= valid_document.size()){
                            Log.d(TAG, "Completed: " + profiles);
                            petProfileQuery = db.collection(COLLECTION_NAME)
                                    .startAfter(lastVisible).limit(downloadLimit);
                            Collections.sort(profiles);
                            toAppend.addAll(profiles);
                            profiles.clear();
                            c.didCompleteDownload();
                        }
                    }
                    @Override
                    public void onItemRangeMoved(ObservableList sender, int fromPosition, int toPosition, int itemCount) {
                        Log.d("onItemRangeMoved",  itemCount +"");
                    }
                    @Override
                    public void onItemRangeRemoved(ObservableList sender, int positionStart, int itemCount) {
                        Log.d("onItemRangeRemoved",  itemCount +"");
                    }
                });
            });
        });
    }

    private boolean user_pets(String pet_id){
        String id = user.getUid();
        return get_userId(pet_id).equals(id);
    }
    private String get_userId(String pet_id){
        return pet_id.substring(0,pet_id.length()-1);
    }
    private boolean relation_pets(String pet_id, Map<String, String> pet_list) {

        if (pet_list.get(pet_id) != null) {
            switch (pet_list.get(pet_id)) {
                case "friend":
                case "2":
                case "3":
                case "0":
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}

