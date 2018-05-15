package com.petclump.petclump.models;

public class BaseMessage {
    public int which_side;
    public String message;
    public String user;

    public int getWhich_side() {
        return which_side;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public BaseMessage(int which_side, String message, String user) {
        this.which_side = which_side;
        this.message = message;
        this.user = user;
    }
}
