package com.petclump.petclump.models;

import android.app.Activity;
import android.content.Context;

import java.io.IOException;
import java.util.Map;

public interface Profile{
    public Map<String, Object> generateDictionary();
    public void upload(Context c);
}