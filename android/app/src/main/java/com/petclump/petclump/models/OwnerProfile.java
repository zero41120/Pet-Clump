package com.petclump.petclump.models;

public class OwnerProfile extends Profile {
    String freeTime;

    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
    }

    @Override
    public Integer calculateMatch(Profile other) {
        return null;
    }

    @Override
    public void uploadToFirebase() {

    }

    @Override
    public void downloadFromFirebase() {

    }
}
