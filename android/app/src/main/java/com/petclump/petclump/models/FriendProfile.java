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
        return (this.name == c.name) && (this.lastMessage == c.lastMessage) && (this.time == c.time)
                &&(this.url == c.url);
    }

}
