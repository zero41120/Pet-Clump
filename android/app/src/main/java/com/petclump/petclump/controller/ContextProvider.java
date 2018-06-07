package com.petclump.petclump.controller;

import android.content.Context;

public class ContextProvider {

    private static Context c;

    public static void setContext(Context c){
        ContextProvider.c = c;
    }

    public static Context getContext(){
        return c;
    }
}
