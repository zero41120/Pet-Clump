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
    public static String specie_num(int n){
        switch(n){
            case 0: return "DOG";
            case 1: return "CAT";
            case 2: return "BIRD";
            case 3: return "ANT";
            case 4: return "FERRET";
            case 5: return "FISH";
            case 6: return "FROG";
            case 7: return "LIZARD";
            case 8: return "GOAT";
            case 9: return "HAMSTER";
            case 10: return "HORSE";
            case 11: return "PIG";
            case 12: return "RABBIT";
            case 13: return "SNAKE";
            case 14: return "TURTLE";
            case 15: return "OCTOPUS";
            case 16: return "LLAMA";
            case 17: return "EAGLE";
            case 18: return "OTHER";
        }
        return "WRONG_ANIMAL";
    }
}
