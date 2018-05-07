package com.petclump.petclump;

import java.util.HashMap;
import java.lang.String;
import java.util.Map;

public class DefaultMap<K, V> extends HashMap<K, V> {

    public DefaultMap(Map<K, V> data){
        this.putAll(data);
    }

    public String getDefault(String key){
        return getDefault(key, "");
    }
    public String getDefault(String key, String defValue){
        Object raw = get(key);
        return raw == null ? defValue: raw.toString();
    }

}
