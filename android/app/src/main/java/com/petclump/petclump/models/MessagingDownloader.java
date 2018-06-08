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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Integer downloadLimit = 2;
    private Query messageQuery;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String combined_id = "";
    private String my_id = "";
    private String other_id = "";
    private Context ctx;
    private byte[] sharedKeys;
    private HashMap<String,Boolean> visited;

    public MessagingDownloader(Activity c, String my_id, String other_id, byte[] key, Integer downloadLimit){
        this.ctx = c;
        this.my_id = my_id;
        this.other_id = other_id;
        this.visited = new HashMap<>();
        if(my_id.compareTo(other_id)>0) {
            combined_id = my_id + other_id;
        } else {
            combined_id = other_id + my_id;
        }
        Log.d(TAG,"combined_id:"+combined_id);
        this.sharedKeys = key;
        this.downloadLimit = downloadLimit;
        messageQuery = db.collection("chats").document(combined_id).collection("message")
                .orderBy("time", Query.Direction.ASCENDING);
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
                    if(temp.get("senderId").toString().equals(my_id))
                        sender = 1;
                    else
                        sender = 2;
                    BaseMessage temp_meg = new BaseMessage(sender,temp.get("text").toString(),new Timestamp((Date)temp.get("time")));
                    messages.add(temp_meg);
                }
                Log.d(TAG, "Completed: " + messages.toString());
                toAppend.addAll(messages);
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
                List A = queryDocumentSnapshots.getDocumentChanges();
                messages.clear();

                String source = queryDocumentSnapshots != null && queryDocumentSnapshots.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";
                if(source.equals("Local"))
                    return;
                // iterate through data
                for(Object x: A){
                    if(x != null){
                        //String doc_id = ((DocumentChange)x).getDocument().getId();
/*                                    File path = new File(ctx.getCacheDir()+"/message/"+combined_id+"/",doc_id+".txt");
                                    if(path.exists())
                                        continue;*/
//                                    if(visited.containsKey(doc_id))
//                                        continue;
                        Map<String, Object> temp = ((DocumentChange)x).getDocument().getData();
                        int sender = 0;
                        if(temp.get("senderId").toString().equals(my_id)) {
                            sender = 1;
                        } else {
                            sender = 2;
                        }
                        byte[] iv = Cryptographer.convertIV(temp.get("iv").toString());
                        Cryptographer cG = Cryptographer.getInstance();
                        String decoded = cG.decrypt(sharedKeys, iv, temp.get("text").toString());;
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

        /*DocumentReference ref = FirebaseFirestore.getInstance().collection("chats").document(combined_id);
        ref.collection("message")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size()>0){
                    ref.collection("message").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen to message failed.", e);
                                return;
                            }
                            List A = queryDocumentSnapshots.getDocumentChanges();
                            String source = queryDocumentSnapshots != null && queryDocumentSnapshots.getMetadata().hasPendingWrites()
                                    ? "Local" : "Server";
                            if(source.equals("Local"))
                                return;
                            // iterate through data
                            for(Object x: A){
                                if(x != null){
                                    //String doc_id = ((DocumentChange)x).getDocument().getId();
*//*                                    File path = new File(ctx.getCacheDir()+"/message/"+combined_id+"/",doc_id+".txt");
                                    if(path.exists())
                                        continue;*//*
//                                    if(visited.containsKey(doc_id))
//                                        continue;
                                    Map<String, Object> temp = ((DocumentChange)x).getDocument().getData();
                                    int sender = 0;
                                    if(temp.get("senderId").toString().equals(my_id))
                                        sender = 1;
                                    else
                                        sender = 2;
                                    BaseMessage temp_meg = new BaseMessage(sender,temp.get("text").toString(),new Timestamp((Date)temp.get("time")));
                                    if(toAppend.contains(temp_meg))
                                        continue;
                                    messages.add(temp_meg);
                                }
                            }
                            toAppend.addAll(messages);
                            c.didCompleteDownload();
                        }
                    });
                }else{
                    Log.d(TAG,combined_id + " No message");
                    c.didCompleteDownload();
                }
            }
        });*/
    }
}
