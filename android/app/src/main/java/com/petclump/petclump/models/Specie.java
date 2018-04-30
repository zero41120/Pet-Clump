package com.petclump.petclump.models;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.petclump.petclump.R;


public enum Specie {
    DOG, CAT, BIRD, ANT, FERRET,FISH, FROG, LIZARD,
    GOAT, HAMSTER, HORSE, PIG, RABBIT, SNAKE, TURTLE,
    OCTOPUS, LLAMA, EAGLE, OTHER;

    public String getName(Context applicationContext){
        Resources R = applicationContext.getResources();
        String packageName = applicationContext.getPackageName();
        int id = R.getIdentifier(this.name(), "string", packageName);
        return R.getString(id);
    }
    public static int num_specie(String spe){
        switch(spe){
            case "DOG": return 0;
            case "CAT": return 1;
            case "BIRD": return 2;
            case "ANT": return 3;
            case "FERRET": return 4;
            case "FISH": return 5;
            case "FROG": return 6;
            case "LIZARD": return 7;
            case "GOAT": return 8;
            case "HAMSTER": return 9;
            case "HORSE": return 10;
            case "PIG": return 11;
            case "RABBIT": return 12;
            case "SNAKE": return 13;
            case "TURTLE": return 14;
            case "OCTOPUS": return 15;
            case "LLAMA": return 16;
            case "EAGLE": return 17;
            case "OTHER": return 18;
        }
        return -1;
    }
}
