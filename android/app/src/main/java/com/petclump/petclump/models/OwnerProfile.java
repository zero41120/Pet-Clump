package com.petclump.petclump.models;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * This is the onwer profile data model
 * - id: this should be the uid from Firebase Auth object
 * - name: this should be user's name to show to public
 * - birthday: this is a Date object. In the initlizer, it's a yyyy/MM/dd string
 * - gender: a string of any gender
 * - distancePerference: this shows the user's perference for matching
 * - lat and lon: for location calculation
 * - freeTime: this is a 7*3 bool matrix for free time. In the initlizer, it's a 21 character string with 1 mark as free.
 */
public class OwnerProfile implements Profile {
    /*** data field ***/
    final String id;
    String name     = "No name";
    String gender   = "Apache";
    Date birthday   = new Date();
    int distancePerference = 5;
    double lat = 0.0 ,lon = 0.0;
    FreeSchedule freeTime = new  FreeSchedule("");

    public OwnerProfile (String id){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            this.id = id;
        } else {
            this.id = "error_id";
        }
    }
    public static String num_month(int num){
        switch(num){
            case 1: return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "Jun";
            case 7: return "Jul";
            case 8: return "Aug";
            case 9: return "Sep";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
        }
        return "month_error "+num;
    }
    @Override
    public Map<String,Object> generateDictionary(){
        Map<String, Object> temp= new HashMap<>();
        temp.put("id", id);
        temp.put("lat",lat);
        temp.put("lon",lon);
        temp.put("name",name);
        temp.put("gender",gender);
        temp.put("birthday",birthday);
        temp.put("freeTime",freeTime.freeString);
        temp.put("distancePerference", distancePerference);
        return temp;
    }
    @Override
    public void upload(Context c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Toast.makeText(c, "OwnerProfile.upload: User not signed in!\n", Toast.LENGTH_SHORT).show();
        }
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("users").document(this.id);
        docRef.set(generateDictionary()).addOnCompleteListener(task -> {
            String message = "";
            if(task.isSuccessful()) {
                message += "Upload successful for user: " + this.id;
            }
            Log.d("Profile", "upload: " + message);
        });

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getDistancePerference() {
        return distancePerference;
    }

    public void setDistancePerference(int distancePerference) {
        this.distancePerference = distancePerference;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public FreeSchedule getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = new FreeSchedule(freeTime);
    }
}