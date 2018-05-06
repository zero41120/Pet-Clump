package com.petclump.petclump.models;

import android.content.Context;

import com.petclump.petclump.ProfileDownloader;
import com.petclump.petclump.ProfileUploader;

import java.util.Map;

public interface Profile{
    public Map<String, Object> generateDictionary();
    public void upload(String id, ProfileUploader c);
    public void download(String id, ProfileDownloader c);
}

