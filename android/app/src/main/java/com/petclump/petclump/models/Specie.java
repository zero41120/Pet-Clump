package com.petclump.petclump.models;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.petclump.petclump.R;


public enum Specie {
    DOG, CAT, BIRD, ANT, FERRET,FISH, FROG, GECKO,
    GOAT, HAMSTER, HORSE, PIG, RABBIT, SNAKE, TURTLE,
    OTHER;


    public String getName(Context applicationContext){
        Resources R = applicationContext.getResources();
        String packageName = applicationContext.getPackageName();
        int id = R.getIdentifier(this.name(), "string", packageName);
        return R.getString(id);
    }

}
