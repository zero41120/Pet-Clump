package com.petclump.petclump.models;

public class FriendNode {
    private String pending = "";
    private String receiver_id = "";
    private String sender_id = "";

    public FriendNode(){}
    public FriendNode(String pending, String receiver_id, String sender_id){
        this.pending = pending;
        this.receiver_id = receiver_id;
        this.sender_id = sender_id;
    }

    public String getPending() {
        return pending;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
