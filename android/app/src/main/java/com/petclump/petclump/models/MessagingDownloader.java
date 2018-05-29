package com.petclump.petclump.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.ArrayList;
import java.util.Map;

public class MessagingDownloader {
    private static final String TAG = "MessagingDownloader";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer downloadLimit = 2;
    private Query messageQuery;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String combined_id = "";

    public MessagingDownloader(String my_id, String other_id, Integer downloadLimit){
        if(my_id.compareTo(other_id)>0)
            combined_id = my_id + other_id;
        else
            combined_id = other_id + my_id;

        this.downloadLimit = downloadLimit;
        messageQuery = db.collection("chats").document(combined_id).collection("message")
                .orderBy("time", Query.Direction.ASCENDING).limit(downloadLimit);
    }

    public void downloadMore(ArrayList<BaseMessage> toAppend, ProfileDownloader c){
            ArrayList<BaseMessage> messages = new ArrayList<>();
            messageQuery.get().addOnSuccessListener(documentSnapshots -> {
                if(documentSnapshots.size() -1 < 0){ return; }
                DocumentSnapshot lastVisible = documentSnapshots.getDocuments()
                        .get(documentSnapshots.size() -1);
                for (DocumentSnapshot doc: documentSnapshots.getDocuments()) {
                    Map<String,Object> temp = doc.getData();
                    int sender = 0;
                    // decide sender
                    if(temp.get("sender").toString().equals("my_id"))
                        sender = 1;
                    else
                        sender = 2;
                    BaseMessage temp_meg = new BaseMessage(sender,temp.get("text").toString(),temp.get("time").toString());
                    messages.add(temp_meg);
                }
                Log.d(TAG, "Completed: " + messages.toString());
                messageQuery = db.collection("chats").document(combined_id).collection("message")
                        .orderBy("time", Query.Direction.ASCENDING).
                        startAfter(lastVisible).limit(downloadLimit);
                toAppend.addAll(messages);
                c.didCompleteDownload();
            });
    }
}
