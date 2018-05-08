package com.petclump.petclump.models;

import android.support.annotation.NonNull;

import java.util.Map;

public class MatchingProfile implements Comparable<MatchingProfile>{

    private PetProfile petProfile;
    private Double matchingPercent;
    private Integer distance;

    public MatchingProfile(String quizResult, PetProfile petProfile) {
        this.petProfile = petProfile;
        matchingPercent = calculatePercent(quizResult, petProfile.getQuiz());
        this.distance = 0;
    }

    public String getPhotoUrl(){
        return this.petProfile.getPhotoUrl(PetProfile.UrlKey.main);
    }

    private Double calculatePercent(String q1, String q2){
        int size = q1.length() < q2.length()? q1.length() : q2.length();
        Double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += q1.charAt(i) == q2.charAt(i)? 1 : 0;
        }
        if (size == 0){
            return 0.0;
        }
        return sum / size;
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
