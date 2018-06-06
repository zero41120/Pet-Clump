package com.petclump.petclump.models;

public class FriendProfile {
    private String name;
    private String lastMessage;
    private String time;
    private String url;
    private String my_id = "";
    private String friend_id = "";
    private String status = "";


    public FriendProfile(String my_id, String friend_id,String name, String lastMessage, String time, String url, String status) {
        this.my_id = my_id;
        this.friend_id = friend_id;
        this.name = name;
        this.lastMessage = lastMessage;
        this.time = time;
        this.url = url;
        this.status = status;

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

    public String getMy_id(){return  my_id;};

    public String getFriend_id() {
        return friend_id;
    }

    public String getFriend_status(){ return status; }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }
        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof FriendProfile)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        FriendProfile c = (FriendProfile) o;

        // Compare the data members and return accordingly
        return (this.friend_id.equals(c.getFriend_id()) && this.my_id.equals(c.getMy_id()));
    }

}
