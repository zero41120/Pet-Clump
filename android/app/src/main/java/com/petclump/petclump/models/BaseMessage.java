package com.petclump.petclump.models;

public class BaseMessage {
    public int which_side;
    public String message;
    public String send_time;

    public int getWhich_side() {
        return which_side;
    }

    public String getMessage() {
        return message;
    }

    public String getSend_time() {
        return send_time;
    }

    public BaseMessage(int which_side, String message, String send_time) {
        this.which_side = which_side;
        this.message = message;
        this.send_time = send_time;
    }
}
