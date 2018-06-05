package com.petclump.petclump.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.petclump.petclump.models.protocols.ProfileDownloader;

import java.util.Map;

public class MatchingProfile implements Comparable<MatchingProfile>{

    private PetProfile petProfile;
    private double quizPercent;
    private double matchingPercent;
    private Integer distance;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private double lat = 0.0;
    private double lon = 0.0;
    private OwnerProfile owner = OwnerProfile.getInstance();
    private final int R = 6373;
    private String TAG = "MatchingProfile:";
    private boolean note = false;

    public MatchingProfile(String quizResult, PetProfile petProfile) {
        this.petProfile = petProfile;
        quizPercent = calculateQuiz(quizResult, petProfile.getQuiz());
        this.distance = 0;
    }

    public String getPhotoUrl(){
        return this.petProfile.getPhotoUrl(PetProfile.UrlKey.main);
    }

    private double calculateQuiz(String q1, String q2){
        int size = q1.length() < q2.length()? q1.length() : q2.length();
        double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += q1.charAt(i) == q2.charAt(i)? 1 : 0;
        }
        if (size == 0){
            return 0.0;
        }
        return (sum / size)*0.7;
    }

    public void calculatePercent(ProfileDownloader downloader){
        db.collection("users").document(petProfile.getOwnerId()).get().addOnCompleteListener((task)->{
            // calculate distance
            Map<String, Object> temp = task.getResult().getData();
            lat = Math.toRadians((double)temp.get("lat"));
            lon = Math.toRadians((double)temp.get("lon"));
            long pre = (long)temp.get("distancePerference");
            double userlon = Math.toRadians(owner.getLon());
            double userlat = Math.toRadians(owner.getLat());

            //Log.d(TAG, "lat2:"+lat+" lon2:"+lon+" lat:"+userlat+" lon:"+userlon);
            double dlon = lon - userlon;
            double dlat = lat - userlat;
            double a = Math.pow(Math.sin(dlat/2.0),2) + Math.cos(userlat)*Math.cos(lat)*Math.pow(Math.sin(dlon/2.0),2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
            this.distance =  ((Double)(R*c)).intValue();

            //Log.d(TAG, "a:"+a+" c:"+c+" d:"+distance);
            // calcualte quiz
            if(distance < pre/2)
                matchingPercent = 0.3+quizPercent;
            else
                matchingPercent = 0.15+quizPercent;
            downloader.didCompleteDownload();
        });
    }

    public String getName(){
        return petProfile.getName();
    }

    public String getAge(){
        return petProfile.getAge();
    }

    public String getBio(){
        return petProfile.getBio();
    }

    public Integer getDistance(){
        return distance;
    }
    public String getMatchingPercent(){
        return Integer.valueOf((int)(matchingPercent * 100)).toString();
    }

    public Map<String, Object> getData(){
        return petProfile.generateDictionary();
    }

    public String getPetId(){
        return petProfile.getOwnerId() + petProfile.getSequence();
    }

    public String getSpe(){
        return petProfile.getSpe();
    }
    public boolean getNote(){
        return note;
    }

    @Override
    public int compareTo(@NonNull MatchingProfile o) {
       if (matchingPercent > o.matchingPercent) {
           return -1;
       } else if (matchingPercent < o.matchingPercent) {
           return 1;
       } else {
           return 0;
       }
    }

    @Override
    public String toString() {
        return getMatchingPercent() + ": " + getName();
    }
}
