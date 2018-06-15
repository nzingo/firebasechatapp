package com.boutik.nadhir.firebasechatapp.models;

public class UserModel {



    private String username;
    private String email;
    private Object Last_seen;
    private boolean online;


    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public UserModel(String username, String email/*, String profile_img*/) {
        this.username = username;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Object getLast_seen() {
        return Last_seen;
    }

    public void setLast_seen(Object last_seen) {
        Last_seen = last_seen;
    }
}
