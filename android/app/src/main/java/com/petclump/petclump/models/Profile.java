package com.petclump.petclump.models;

import android.content.Context;

import com.petclump.petclump.ProfileUpdator;

import java.util.Map;

public interface Profile{
    public Map<String, Object> generateDictionary();
    public void upload(String id, Context c);
    public void download(String id, ProfileUpdator c);
}