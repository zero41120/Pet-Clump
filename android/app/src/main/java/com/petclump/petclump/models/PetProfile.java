package com.petclump.petclump.models;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.petclump.petclump.R;
import com.petclump.petclump.models.Cryptography.Cryptographer;
import com.petclump.petclump.models.Cryptography.KeyExchanger;
import com.petclump.petclump.models.datastructures.DefaultMap;
import com.petclump.petclump.models.protocols.FriendChangeState;
import com.petclump.petclump.models.protocols.Profile;
import com.petclump.petclump.models.protocols.ProfileDeletor;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.models.protocols.ProfileUploader;

import java.math.BigInteger;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

public class PetProfile implements Profile {
    private String bio = "PET_BIO";
    private String age = "0";
    private String spe = "CAT";
    private String name = "Guko";
    private String owner_id = "null";
    private String quiz = "";
    private Integer sequence = -1;
    private String TAG = "PetProfile";
    private HashMap<String, String> relation_list = new HashMap<>();    // friend_list
    private HashMap<String, String> chat_list = new HashMap<>();        // chat_list


    public static final int default_image = R.drawable.dog_placeholder;



    // pet photo url
    private HashMap<String, String> url_map = new HashMap<String, String>(){{
        put("main_profile_url","");
        put("pet_profile_url_1","");
        put("pet_profile_url_2","");
        put("pet_profile_url_3","");
        put("pet_profile_url_4","");
        put("pet_profile_url_5","");
        put("group_profile_url_1","");
        put("group_profile_url_2","");
        put("group_profile_url_3","");
    }};


    // firebase instance
    private FirebaseAuth Auth_pet = FirebaseAuth.getInstance();
    private FirebaseStorage Store_pet = FirebaseStorage.getInstance();

    public PetProfile(){}

    public PetProfile(Map<String, Object> map){
        DefaultMap data = new DefaultMap(map);
        this.bio = data.getDefault("bio");
        this.age = data.getDefault("age");
        this.spe = data.getDefault("spe");
        this.name = data.getDefault("name");
        this.owner_id = data.getDefault("owner_id");
        this.sequence = Integer.parseInt(data.getDefault("sequence"));
        this.quiz = data.getDefault("quiz");
        url_map.put("main_profile_url",data.getDefault("main_url"));
        url_map.put("pet_profile_url_1",data.getDefault("pet_view_1"));
        url_map.put("pet_profile_url_2",data.getDefault("pet_view_2"));
        url_map.put("pet_profile_url_3",data.getDefault("pet_view_3"));
        url_map.put("pet_profile_url_4",data.getDefault("pet_view_4"));
        url_map.put("pet_profile_url_5",data.getDefault("pet_view_5"));
        url_map.put("group_profile_url_1",data.getDefault("group_view_1"));
        url_map.put("group_profile_url_2",data.getDefault("group_view_2"));
        url_map.put("group_profile_url_3",data.getDefault("group_view_3"));
    }

    public Map<String,Object> generateDictionary(){
        return new HashMap<String, Object>(){{
            put("bio", bio);
            put("age", age);
            put("spe", spe);
            put("name",name);
            put("owner_id",owner_id);
            put("sequence",sequence);
            put("quiz",quiz);
            put("main_url", url_map.get("main_profile_url"));
            put("pet_view_1", url_map.get("pet_profile_url_1"));
            put("pet_view_2",url_map.get("pet_profile_url_2"));
            put("pet_view_3",url_map.get("pet_profile_url_3"));
            put("pet_view_4",url_map.get("pet_profile_url_4"));
            put("pet_view_5",url_map.get("pet_profile_url_5"));
            put("group_view_1",url_map.get("group_profile_url_1"));
            put("group_view_2",url_map.get("group_profile_url_2"));
            put("group_view_3",url_map.get("group_profile_url_3"));
        }};
    }

    private Map<String, Object> generateFriendRequest(String pending){
        return new HashMap<String, Object>(){{
            put("pending", pending);

        }};
    }
    // friend manipulation field
    public enum friend_change_type {NEW_FRIEND, ADD_UNREAD_FRIEND, BLOCK_FRIEND};
    public void new_friend_change(String sender_id, String receiver_id, friend_change_type type, Context ctx, FriendChangeState c){
        if (Auth_pet.getCurrentUser() == null){
            Log.e(TAG, "user is null.");
            return;
        }
        String code_sender = "";
        String code_receiver = "";
        switch (type){
            case NEW_FRIEND:
                code_sender = "sending";
                code_receiver = "receiving";
                update_friend_key(sender_id, receiver_id,type,ctx,()->{});
                break;
            case ADD_UNREAD_FRIEND:
                code_sender = "friending";
                code_receiver = "friending";
                update_friend_key(sender_id, receiver_id,type,ctx,()->{});
                break;
            case BLOCK_FRIEND:
                code_sender = "blocking";
                code_receiver = "blocking";
                break;
        }

        DocumentReference sender_list = FirebaseFirestore.getInstance().collection("pets")
                .document(sender_id)
                .collection("friends").document(receiver_id);
        DocumentReference receiver_list = FirebaseFirestore.getInstance().collection("pets")
                .document(receiver_id)
                .collection("friends").document(sender_id);
        String finalCode_receiver = code_receiver;
        // upload sender's list
        sender_list.set(generateFriendRequest(code_sender)).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.w(TAG, "changing friend status failed.");
            }else{
                // upload receiver's list
                receiver_list.set(generateFriendRequest(finalCode_receiver)).addOnCompleteListener(task2 -> {
                    if(task2.isSuccessful()) {

                        c.didCompleteChange();
                    }
                    else {
                        Log.w(TAG, "changing friend status failed.");
                    }
                });
            }
        });
    }
    public void update_friend_key(String myPetId, String friendPetId, friend_change_type type, Context ctx, ProfileUploader c){

        if(type == friend_change_type.NEW_FRIEND){
            // thispet key setup
            KeyExchanger thispet = new KeyExchanger(myPetId);
            String bigPrime = thispet.getBigPrime().toString();
            String priPrime = thispet.getPrimitiveRoot().toString();
            String thepetPublic = thispet.getMyPublic().toString();
            FirebaseFirestore.getInstance().collection("chats").document(getCombinedId(myPetId, friendPetId)).set(new HashMap<String, Object>(){{
                put(myPetId, thepetPublic);
                put(friendPetId, "?");
                put("bigPrime", bigPrime);
                put("priPrime", priPrime);
            }}).addOnCompleteListener(task->{
                if(task.isSuccessful()){
                    c.didCompleteUpload();
                }
            });
        }else if(type == friend_change_type.ADD_UNREAD_FRIEND){
            // thispet key setup
            DocumentReference charRef = FirebaseFirestore.getInstance().collection("chats").document(getCombinedId(myPetId, friendPetId));
            charRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot snap = task.getResult();
                    if (!snap.exists()){
                        Log.d(TAG, "update_friend_key: not exists");
                    }
                    try {
                        Map<String, Object> data = snap.getData();
                        Log.d(TAG, "update_friend_key: " + data);
                        KeyExchanger kE = new KeyExchanger(friendPetId, data);
                        data.put(friendPetId, kE.getMyPublic().toString());
                        Log.d(TAG, "update_friend_key in add unread fried: " + data);
                        charRef.set(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }else{
            Log.d(TAG,"update_friend_key: wrong friend_change_type");
            return;
        }
    }
    public void friend_delete (String sender_id, String receiver_id, ProfileDeletor c){
        if (Auth_pet.getCurrentUser() == null){
            Log.e(TAG, "user is null.");
            return;
        }
        if(relation_list.containsKey(sender_id))
            relation_list.remove(sender_id);
        if(relation_list.containsKey(receiver_id))
            relation_list.remove(receiver_id);
        // delete receiver from sender
        FirebaseFirestore.getInstance().collection("pets")
                .document(sender_id)
                .collection("friends")
                .document(receiver_id).get().addOnCompleteListener(task->{
                    if(task.getResult().exists()){
                        task.getResult().getReference().delete();

                        // delete sender from receiver
                        FirebaseFirestore.getInstance().collection("pets")
                                .document(receiver_id)
                                .collection("friends")
                                .document(sender_id).get().addOnCompleteListener(task2->{
                            if(task2.getResult().exists()){
                                task2.getResult().getReference().delete();

                                // delete chatRoom
                                FirebaseFirestore.getInstance().collection("chats")
                                        .document(getCombinedId(sender_id,receiver_id)).get().addOnCompleteListener(task3->{
                                    if(task3.getResult().exists()){
                                        task3.getResult().getReference().delete();

                                        c.didCompleteDelete();
                                    }else {
                                        Log.e(TAG, "friend_delete: chat_room doesn't exist");
                                    }
                                });

                            }else {
                                Log.e(TAG, "friend_delete: sender_id doesn't exist");
                            }
                        });
                    }else{
                        Log.e(TAG,"friend_delete: receiver_id doesn't exist");
                    }
            });


    }
    public void listenToFriendList(String pet_id, ProfileDownloader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, "listenToFriendList:"+" current user is none");
            return;
        }
        DocumentReference ref = FirebaseFirestore.getInstance().collection("pets").document(pet_id);
        ref.collection("friends")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.getResult().size()>0){
                    ref.collection("friends").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen to Friend_List failed.", e);
                                return;
                            }
                            List A = queryDocumentSnapshots.getDocumentChanges();
                            // iterate through data
                            for(Object x: A){
                                Map<String, Object> ref_data = x==null? null: ((DocumentChange)x).getDocument().getData();
                                if(x != null)
                                    relation_list.put(((DocumentChange)x).getDocument().getId(), ref_data.get("pending").toString());
                            }
                            //Log.d(TAG,"FriendList:" +Thread.currentThread().toString());
                            c.didCompleteDownload();
                        }
                    });
                }else{
                    Log.d(TAG,pet_id + " doesn't contain friends collection");
                    c.didCompleteDownload();
                }
            }
        });
    }
    public HashMap<String, String> getRelation_list() {
        return relation_list;
    }

    // message manipulation field
    public void listenToChatList(String pet_id, ProfileDownloader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, "listenToChatList:"+" current user is none");
            return;
        }
        DocumentReference ref = FirebaseFirestore.getInstance().collection("pets").document(pet_id);
        ref.collection("unread")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.getResult().size()>0){
                    ref.collection("unread").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            if (e != null) {
                                Log.w(TAG, "Listen to Chat_List failed.", e);
                                return;
                            }
                            List A = queryDocumentSnapshots.getDocumentChanges();
                            // iterate through data
                            for(Object x: A){
                                Map<String, Object> ref_data = x==null? null: ((DocumentChange)x).getDocument().getData();
                                if(x != null)
                                    chat_list.put(((DocumentChange)x).getDocument().getId(), ref_data.get("pending").toString());
                            }
                            //Log.d(TAG,"FriendList:" +Thread.currentThread().toString());
                            c.didCompleteDownload();
                        }
                    });
                }else{
                    Log.d(TAG,pet_id + " No unread chats");
                    c.didCompleteDownload();
                }
            }
        });
    }
    public void new_unread_message(String sender_id, String receiver_id, ProfileUploader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.e(TAG, "user is null.");
            return;
        }
        if(sender_id.equals("") || receiver_id.equals("")){
            Log.e(TAG, "new_unread_message:"+" Error, one of the id is empty!");
            return;
        }
        String combined_id = "";
        if(sender_id.compareTo(receiver_id)>0){
            combined_id = sender_id + receiver_id;
        }else{
            combined_id = receiver_id + sender_id;
        }

        // upload change
        DocumentReference ref = FirebaseFirestore.getInstance().collection("pets")
                .document(receiver_id)
                .collection("unread").document(combined_id);

        HashMap<String, Object> temp = new HashMap<String, Object>(){{put("pending", "UNREAD");}};
        ref.set(temp).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.w(TAG, "new_unread_message changes failed");
            }
            c.didCompleteUpload();
        });
    }
    public void unread_delete(String unhandled_id, String the_other_id, ProfileDeletor c){
        // notice we will only delete the document from unhandled_id
        if (Auth_pet.getCurrentUser() == null){
            Log.e(TAG, "user is null.");
            return;
        }
        if(unhandled_id.equals("") || the_other_id.equals("")){
            Log.e(TAG, "unread_delete:"+" Error, one of the id is empty!");
            return;
        }
        String combined_id = "";
        if(unhandled_id.compareTo(the_other_id)>0){
            combined_id = unhandled_id + the_other_id;
        }else{
            combined_id = the_other_id + unhandled_id;
        }
        if(chat_list.containsKey(combined_id)){
            chat_list.remove(combined_id);
        }
        // delete receiver from sender
        FirebaseFirestore.getInstance().collection("pets")
                .document(unhandled_id)
                .collection("unread")
                .document(combined_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                // if the document exists
                if(task.getResult().exists()){
                    task.getResult().getReference().delete();
                }else{
                    Log.e(TAG,"unread_delete: document_id doesn't exist");
                }

                c.didCompleteDelete();
            }
        });
    }

    public void newMessage(String my_id, String friend_id, String iv, String cipher, Timestamp time, ProfileUploader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.e(TAG, "user is null.");
            return;
        }

        if(my_id.equals("") || friend_id.equals("")){
            Log.e(TAG, "new_unread_message:"+" Error, one of the id is empty!");
            return;
        }
        String combined_id = getCombinedId(my_id, friend_id);
        // send message
        DocumentReference ref = FirebaseFirestore.getInstance().collection("chats")
                .document(combined_id)
                .collection("message").document();
        Log.d(TAG, "new_message: iv str: " + iv);
        //TODO: encryp text
        HashMap<String, Object> temp = new HashMap<String, Object>(){{
            put("senderId", my_id);
            put("time", time);
            put("text",cipher);
            put("iv", iv);
        }};
        ref.set(temp).addOnCompleteListener(task -> {
            if(!task.isSuccessful()) {
                Log.w(TAG, "new_unread_message changes failed");
            }
            c.didCompleteUpload();
        });
    }

    public HashMap<String, String> getChat_list(){ return chat_list;}


    /*** profile methods ***/
    public void upload(String id, ProfileUploader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.w(TAG, "User is null! Cannot update.");
            return;
        }
        // upload documents
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("pets").document(id);
        docRef.set(generateDictionary()).addOnCompleteListener(task -> {
            String message = "";
            if(task.isSuccessful()) {
                message += "Upload successful for pet: " + this.name;
                c.didCompleteUpload();
            }
            else {message += "Upload fails for pet " + this.name;}
            Log.d("Profile", "upload: " + message);
        });
    }

    @Override
    public void download(String id, ProfileDownloader c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, "download:"+" Current user is none");
            return;
        }
        DocumentReference mDocRef = FirebaseFirestore.getInstance().collection("pets").document(id);

        mDocRef.addSnapshotListener((snap, error) -> {
            if (error != null) {
                Log.d(TAG, "Download failed: " + error.toString());
                return;
            }
            if (snap == null)   { return; }
            if (!snap.exists()) { return; }
            DefaultMap<String, Object> ref = new DefaultMap<String, Object>(snap.getData());
            this.bio = ref.getDefault("bio");
            this.age = ref.getDefault("age");
            this.spe = ref.getDefault("spe");
            this.name = ref.getDefault("name");
            this.owner_id = ref.getDefault("owner_id");
            this.sequence = Integer.parseInt(ref.getDefault("sequence"));
            this.quiz = ref.getDefault("quiz");
            url_map.put("main_profile_url",ref.getDefault("main_url"));
            url_map.put("pet_profile_url_1",ref.getDefault("pet_view_1"));
            url_map.put("pet_profile_url_2",ref.getDefault("pet_view_2"));
            url_map.put("pet_profile_url_3",ref.getDefault("pet_view_3"));
            url_map.put("pet_profile_url_4",ref.getDefault("pet_view_4"));
            url_map.put("pet_profile_url_5",ref.getDefault("pet_view_5"));
            url_map.put("group_profile_url_1",ref.getDefault("group_view_1"));
            url_map.put("group_profile_url_2",ref.getDefault("group_view_2"));
            url_map.put("group_profile_url_3",ref.getDefault("group_view_3"));
            //Log.d(TAG, "url_map\n"+url_map.toString());
            c.didCompleteDownload();
        });
    }

    public void delete(String id, ProfileDeletor c){
        if (Auth_pet.getCurrentUser() == null){
            Log.d(TAG, " Current User is none");
            return;
        }
        //delete Photo
        deletePhoto("main_profile_url", ()->{});
        deletePhoto("pet_profile_url_1", ()->{});
        deletePhoto("pet_profile_url_2", ()->{});
        deletePhoto("pet_profile_url_3", ()->{});
        deletePhoto("pet_profile_url_4", ()->{});
        deletePhoto("pet_profile_url_5", ()->{});
        deletePhoto("group_profile_url_1", ()->{});
        deletePhoto("group_profile_url_2", ()->{});
        deletePhoto("group_profile_url_3", ()->{});
        FirebaseFirestore.getInstance().collection("pets").document(id).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        c.didCompleteDelete();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });;
    }

    public void deletePhoto(String t, ProfileDeletor c){
        String temp = url_map.get(t);
        Log.d(TAG,"delete photo url " + temp);
        if(temp.compareTo("") == 0){
            return;
        }

        String path = "image/" + parseUrl(url_map.get(t));
        // Create a storage reference from our app
        Log.d(TAG,"path:"+path);
        StorageReference storageRef = Store_pet.getReference(path);

        // Delete the file
        storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                c.didCompleteDelete();
            }
        });
        url_map.put(t,"");
    }

    public void setPhoto(String t, byte[] data, ProfileUploader c){
        String path = "image/" + UUID.randomUUID() + ".png";
        StorageReference storageRef = Store_pet.getReference(path);
        // upload the file
        UploadTask uploadTask = storageRef.putBytes(data);
        // find the download url
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageRef.getDownloadUrl();
            }
        }).addOnCompleteListener((Task<Uri> task)->{
            String url = task.getResult().toString();
            Log.d(TAG,"URLTask:"+url);
            url_map.put(t,url);
        });

        c.didCompleteUpload();
    }
    private String parseUrl(String url){

        Pattern pattern = Pattern.compile(".*%2[fF](.*)\\?.*");
        Matcher matcher = pattern.matcher(url);

        if(matcher.matches()){
            //Log.d(TAG,"filename:"+matcher.group(1));
            return matcher.group(1);

        }

        return "";
    }

    public String getOwnerId() {
        return owner_id;
    }
    public String getName() {
        return name;
    }
    public String getSpe(){return spe;}
    public String getBio(){return bio;}
    public String getAge(){return age;}
    public String getQuiz(){return quiz;}
    public String getUrl(String tag){return url_map.get(tag);}
    public Integer getSequence() { return sequence; }

    public void setOwner_id(String owner_id){this.owner_id = owner_id;}
    public void setName(String name){this.name = name;}
    public void setSpe(String spe){this.spe = spe;}
    public void setBio(String bio){this.bio = bio;}
    public void setAge(String age){this.age = age;}
    public void setSequence(int s){this.sequence = s;}
    public void setQuiz(String quiz){ this.quiz = quiz; }
    @Override
    public String toString(){
        return  generateDictionary().toString();
    }

    public enum UrlKey{
        main, pet1, pet2, pet3, pet4, pet5, group1, group2, group3
    }
    public String getPhotoUrl(UrlKey key){
        switch (key) {
            case pet1: return url_map.get("pet_profile_url_1");
            case pet2: return url_map.get("pet_profile_url_2");
            case pet3: return url_map.get("pet_profile_url_3");
            case pet4: return url_map.get("pet_profile_url_4");
            case pet5: return url_map.get("pet_profile_url_5");
            case group1: return url_map.get("group_profile_url_1");
            case group2: return url_map.get("group_profile_url_2");
            case group3: return url_map.get("group_profile_url_3");
        }
        return url_map.get("main_profile_url");
    }
    public static String parseUrlToCache(String url){
        StringBuilder newstr = new StringBuilder();
        for(int i=0; i<url.length();i++){
            if(url.charAt(i) != '/')
                newstr.append(url.charAt(i));
        }
        return newstr.toString();
    }
    public static String getCombinedId(String pet_id1, String pet_id2){
        return (pet_id1.compareTo(pet_id2)>0 ? (pet_id1+pet_id2):(pet_id2+pet_id1));
    }
}
