package com.petclump.petclump.models;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.models.protocols.Profile;
import com.petclump.petclump.models.protocols.ProfileDownloader;
import com.petclump.petclump.models.protocols.ProfileUploader;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
    private String name     = "No name";
    private String gender   = "Apache";
    private Date birthday   = new Date();
    private int distancePerference = 5;
    private double lat = 0.0 ,lon = 0.0;
    private FreeSchedule freeTime = new FreeSchedule("");

    private String TAG = "Ownerprofile";

    // singleton
    private static OwnerProfile obj = new OwnerProfile();
    private OwnerProfile (){}


    public static OwnerProfile getInstance(){
        return obj;
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
        return  new HashMap<String,Object>(){{
            put("lat", lat);
            put("lon", lon);
            put("name", name);
            put("gender", gender);
            put("birthday", birthday);
            put("freeTime", freeTime.freeString);
            put("distancePerference", distancePerference);
        }};

    }
    @Override
    public void upload(String id, ProfileUploader c){
        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Log.d(TAG, "upload_failed due to null user! ");
        }
        FirebaseFirestore.getInstance().collection("users").document(id).set(generateDictionary()).addOnCompleteListener(task -> {
            //Log.d("Profile", "upload: " + message);
            c.didCompleteUpload();
        });
    }
    @Override
    public void download(String id, ProfileDownloader c){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String TAG = "OwnerProfile_"+name;
        if (user == null){ Log.w(TAG,"empty user");  return; }
        FirebaseFirestore.getInstance().collection("users").document(id).get().addOnCompleteListener(task->{
            if (!task.isSuccessful()) {
                Log.d(TAG, "Download failed: " + user.getUid());
                return;
            }

            Map<String, Object> ref = task.getResult().getData();
            this.name = (String) ref.get("name");
            Log.d(TAG, "Download successfully" + name);
            this.gender = (String) ref.get("gender");
            Log.d(TAG, "Download successfully" + gender);
            this.birthday = (Date) ref.get("birthday");
            Log.d(TAG, "Download successfully" + String.valueOf(birthday));
            this.distancePerference = Integer.parseInt(ref.get("distancePerference").toString());
            this.lat = (Double)ref.get("lat");
            this.lon = (Double)ref.get("lon");
            this.freeTime = new FreeSchedule((String) ref.get("freeTime"));

            c.didCompleteDownload();
        });


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
    public double getLon() {
        return lon;
    }
    public void setLat(double lat) {
        this.lat = lat;
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