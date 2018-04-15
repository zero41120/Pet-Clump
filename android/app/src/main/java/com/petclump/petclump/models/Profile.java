package com.petclump.petclump.models;

import android.location.Location;

import java.io.File;
import java.util.List;

public abstract class Profile implements DatabaseLoadable{
    protected String name, gender;
    protected Integer age;
    protected Location city;
    protected List<File> publicPictureFiles, privatePictureFiles;
    protected List<Quiz> quizResults;

    /**
     * This method calculates the distance between 2 profiles
     * @param other the profile to compare
     * @return the distance between 2 profiles in km or mile from user locale
     */
    public Integer getDistance(Profile other) {
        // TODO implement km mile for user
        return 0;
    }

    /**
     * This method calculates the match percentage of 2 profiles.
     * @param other the profile to compare
     * @return the percentile of match of 2 profiles from 1-100
     */
    abstract public Integer calculateMatch(Profile other);

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public Integer getAge() {
        return age;
    }

    public Location getCity() {
        return city;
    }

    public List<File> getPublicPictureFiles() {
        return publicPictureFiles;
    }

    public List<File> getPrivatePictureFiles() {
        return privatePictureFiles;
    }

    public List<Quiz> getQuizResults() {
        return quizResults;
    }
}
