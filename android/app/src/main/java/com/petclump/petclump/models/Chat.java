package com.petclump.petclump.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.petclump.petclump.models.protocols.ChatListener;

import java.util.HashMap;

import javax.annotation.Nullable;

public class Chat {
    private String sender_id = "";
    private String receiver_id = "";
    private String room_number = "";
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String TAG = "Chat";
    private DocumentReference chat_file = null;
    public static DocumentReference server = FirebaseFirestore.getInstance().collection("chats").document("server");

    public Chat(){}
    public Chat (String sender_id, String receiver_id){
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
    }

    public void newChat(){
        this.chat_file = FirebaseFirestore.getInstance().collection("chats").document();
        this.room_number = chat_file.getId();
    }
    public void start(ChatListener c){
        //chat_file.addSnapshotListener();
/*                addSnapshotListener { documentSnapshot, e ->
                when {
            e != null -> Log.e("ERROR", e.message)
            documentSnapshot != null && documentSnapshot.exists() -> {
                with(documentSnapshot) {
                    textDisplay.text = "${data[NAME_FIELD]}:${data[TEXT_FIELD]}"
                }
            }
        }
        }*/
    }

    public DocumentReference getChat_file() {
        return chat_file;
    }
    public void connectServer(ChatListener c){
        if(user == null){
            Log.d(TAG,"ServerListner:"+"not logged in.");
            return;
        }
        server.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                        ? "Local" : "Server";

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, source + " data: " + snapshot.getData());
                } else {
                    Log.d(TAG, source + " data: null");
                }
            }
        });
    }
    public void sendToServer(Object receiver, ChatListener c){
        server.set(new HashMap<String, Object>(){{put(sender_id,receiver);}}).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                //c.didCompleteSend();
            }
            else {
                Log.d(TAG, "sendToServer: "+" failed");
            }
        });
    }
}
