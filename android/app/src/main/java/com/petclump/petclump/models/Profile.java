package com.petclump.petclump.models;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.petclump.petclump.ProfileUIUpdator;

import java.io.IOException;
import java.util.Map;

public interface Profile{
    public Map<String, Object> generateDictionary();
    public void upload(String id, Context c);
    public void download(String id, ProfileUIUpdator c);
}