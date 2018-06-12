package com.boutik.nadhir.firebasechatapp.models;

import java.util.Map;

public class ThreadModel {
    private String uid;
    private String last_message;
    private Map<String, String> time_stamp;


    public ThreadModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ThreadModel(String uid, String last_message, Map<String, String> time_stamp) {
        this.uid = uid;
        this.last_message = last_message;
        this.time_stamp = time_stamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLast_message() {
        return last_message;
    }

    public void setLast_message(String last_message) {
        this.last_message = last_message;
    }

    public Map<String, String> getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Map<String, String> time_stamp) {
        this.time_stamp = time_stamp;
    }
}
