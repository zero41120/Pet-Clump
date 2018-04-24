package com.petclump.petclump.models;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class OwnerProfile  implements Profile {
    /*** data field ***/
    String id="";
    String name = "";
    String gender = "";
    Date birthday;
    int distancePerference;
    double lat,lon;
    FreeSchedule freeTime;

    public OwnerProfile (String id){
        String uid = "";
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(uid != "")
            this.id = uid;
        else
            this.id = "error_id";
    }
    public OwnerProfile(String id, String name, String bdString, String ftString,
                     String gender, int disPerf, double lat, double lon){
        this.id = id;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        try{
            this.birthday = formatter.parse(bdString);
        }catch(ParseException e){
            e.printStackTrace();
            Log.d("OwnerProfile","OwnerProfile contructor receives wrong format of birthday\n");
        }
        this.freeTime = new FreeSchedule(ftString);
        this.name = name;
        this.gender = gender;
        this.lat = lat;
        this.lon = lon;
        this.distancePerference = disPerf;
    }
    @Override
    public Map<String,Object> generateDictionary(){
        Map<String, Object> temp= new HashMap<String, Object>();
        temp.put("id", id);
        temp.put("lat",lat);
        temp.put("lon",lon);
        temp.put("name",name);
        temp.put("gender",gender);
        temp.put("birthday",birthday);
        temp.put("freeTime",freeTime);
        temp.put("distancePerference", distancePerference);
        return temp;
    }
    /*@Override
    public void upload()throws IOException{
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            throw new IOException("OwnerProfile.upload: User not signed in!\n");
        }
        DocumentReference docRef = FirebaseFirestore.getInstance().document("users/" + this.id);
        docRef.set(generateDictionary()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), R.string.Upload_successful, Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), R.string.Upload_failed, Toast.LENGTH_SHORT).show();
            }
        });

    }*/
}