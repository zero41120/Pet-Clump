package com.petclump.petclump.models;

public class FreeSchedule{
    boolean[][] freeMatrix;

    String freeString;


    public boolean isFree(int weekDay, int partDay){
        return freeMatrix[weekDay-1][partDay-1];
    }

    public FreeSchedule(String freeString){
        this.freeString = freeString;
        // initialize freeMatrix
        freeMatrix = new boolean[7][3];
        for(int i=0; i<7; i++){
            for(int j=0; j<3; j++){
                freeMatrix[i][j] = false;
            }
        }
        String chars = new String(freeString);
        int manCounter = 0, weekCounter = 0;
        if (chars.length() != 21) {
            chars = "000000000000000000000";
        }
        for(int i=0; i<freeString.length(); i++){
            // 0 is not free, 1 is free
            freeMatrix[weekCounter][manCounter] = chars.charAt(i) == '1';
            manCounter += 1;
            manCounter %= 3;
            if (manCounter == 0) { weekCounter += 1; }
        }
    }
    @Override
    public String toString(){
        return this.freeString;
    }

}