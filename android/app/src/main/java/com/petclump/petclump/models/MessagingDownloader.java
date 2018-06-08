package com.petclump.petclump.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.petclump.petclump.models.Cryptography.Cryptographer;
import com.petclump.petclump.models.Cryptography.KeyExchanger;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MessagingDownloader {
    private static final String TAG = "MessagingDownloader";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer downloadLimit = 2;
    private Query messageQuery;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String combined_id = "";
    private String my_id = "";
    private String other_id = "";
    private Context ctx;
    private byte[] sharedKeys;
    private HashMap<String,Boolean> visited;

    public MessagingDownloader(String my_id, String other_id, byte[] key){
        this.my_id = my_id;
        this.other_id = other_id;
        this.visited = new HashMap<>();
        this.combined_id = PetProfile.getCombinedId(my_id,other_id);
        Log.d(TAG,"combined_id:"+combined_id);
        this.sharedKeys = key;
        messageQuery = db.collection("chats").document(combined_id).collection("message")
                .orderBy("time", Query.Direction.ASCENDING);
    }

    public static void setlastMessage(String the_pet_id, String the_other_pet_id, byte[] sharedKeys, FriendProfile base, ProfileDownloader c){
            db.collection("chats").document(PetProfile.getCombinedId(the_pet_id, the_other_pet_id))
                    .collection("message").orderBy("time", Query.Direction.DESCENDING)
                    .limit(1).addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if(e != null){
                        base.setLastMessage("");
                        base.setTime("");
                    }else{
                        for(DocumentChange x: queryDocumentSnapshots.getDocumentChanges()){
                            Map<String, Object> ref = x.getDocument().getData();
                            String decoded = decodeString(ref.get("iv").toString(),ref.get("text").toString(),sharedKeys);
                            base.setLastMessage(decoded);
                            base.setTime(ref.get("time").toString());
                        }
                    }
                    c.didCompleteDownload();
            });
    }
    public void listenToRoom(ArrayList<BaseMessage> toAppend, ProfileDownloader c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d(TAG, "listenToMessage:"+" current user is none");
            return;
        }
        ArrayList<BaseMessage> messages = new ArrayList<>();
        messageQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen to message failed.", e);
                    return;
                }
                messages.clear();

                String source = queryDocumentSnapshots != null && queryDocumentSnapshots.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if(source.equals("Local"))
                    return;
                // iterate through data
                for(DocumentChange x: queryDocumentSnapshots.getDocumentChanges()){
                    if(x != null){
                        Map<String, Object> temp = x.getDocument().getData();
                        int sender = 0;
                        if(temp.get("senderId").toString().equals(my_id)) {
                            sender = 1;
                        } else {
                            sender = 2;
                        }
                        String decoded = decodeString(temp.get("iv").toString(),temp.get("text").toString(), sharedKeys);
                        Log.d(TAG, "did decode: " + decoded);
                        BaseMessage temp_meg = new BaseMessage(sender,decoded,new Timestamp((Date)temp.get("time")));
                        if(toAppend.contains(temp_meg))
                            continue;
                        messages.add(temp_meg);
                    }
                }
                Log.d(TAG,"ListenToRoom:"+messages);
                toAppend.addAll(messages);
                Log.d(TAG, "toAppend" + toAppend);
                c.didCompleteDownload();
            }
        });

    }
    private static String decodeString(String iv_, String text, byte[] sharedKeys){
        byte[] iv = Cryptographer.convertIV(iv_);
        Cryptographer cG = Cryptographer.getInstance();
        String decoded = cG.decrypt(sharedKeys, iv, text);
        return decoded;
    }
}
