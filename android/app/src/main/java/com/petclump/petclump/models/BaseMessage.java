package com.petclump.petclump.models;

public class BaseMessage {
    public int which_side;
    public String message;
    public String time;

    public int getWhich_side() {
        return which_side;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return time;
    }

    public BaseMessage(int which_side, String message, String time) {
        this.which_side = which_side;
        this.message = message;
        this.time= time;
    }
    @Override
    public String toString(){
        return "sender:"+which_side+"\n message:"+message+"\n time:"+time+"\n";
    }
}
