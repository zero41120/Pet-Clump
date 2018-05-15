package com.petclump.petclump.models;

public class FriendProfile {
    String name;
    String lastMessage;
    String time;


    public FriendProfile(String name, String lastMessage, String time) {
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;

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

}
