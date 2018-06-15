package com.boutik.nadhir.firebasechatapp.models;

import java.util.Map;

public class ThreadModel {
    private String uid;
    private String name;
    private String profile_img;
    private String last_message;
    private Object time_stamp;


    public ThreadModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ThreadModel(String uid, String name, String profile_img, String last_message, Object time_stamp) {
        this.uid = uid;
        this.name = name;
        this.profile_img = profile_img;
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

    public Object getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Object time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile_img() {
        return profile_img;
    }

    public void setProfile_img(String profile_img) {
        this.profile_img = profile_img;
    }
}
