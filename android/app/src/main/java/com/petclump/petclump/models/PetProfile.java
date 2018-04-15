package com.petclump.petclump.models;


// TODO document this class
public class PetProfile extends Profile implements Animal{

    @Override
    public Specie getSpecie() {
        return null;
    }

    @Override
    public Double getWeight() {
        return null;
    }

    @Override
    public Double getHeight() {
        return null;
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
