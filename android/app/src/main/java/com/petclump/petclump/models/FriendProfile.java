package com.petclump.petclump.models;

public class FriendProfile {
    String name;
    String lastMessage;
    String time;
    String url;


    public FriendProfile(String name, String lastMessage, String time, String url) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.url = url;

    }

    public String getName() {
        return name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getTime() {
        return time;
    }

    public String getUrl(){return url;}

}
