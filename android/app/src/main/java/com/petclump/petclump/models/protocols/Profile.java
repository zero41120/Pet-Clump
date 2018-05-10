package com.petclump.petclump.models.protocols;

import java.util.Map;

public interface Profile{
    public Map<String, Object> generateDictionary();
    public void upload(String id, ProfileUploader c);
    public void download(String id, ProfileDownloader c);
}

