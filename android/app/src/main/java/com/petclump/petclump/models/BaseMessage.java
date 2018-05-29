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
    @Override
    public boolean equals(Object o){
        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof BaseMessage)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        BaseMessage c = (BaseMessage) o;

        // Compare the data members and return accordingly
        return this.time.equals(c.time);
    }
}
