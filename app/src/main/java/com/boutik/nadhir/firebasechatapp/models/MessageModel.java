package com.boutik.nadhir.firebasechatapp.models;

import java.util.Map;

public class MessageModel {
    private String text;
    private String uid;
    private Map<String, String> time_stamp;

    public MessageModel() {
    }

    public MessageModel(String uid, String text, Map<String, String> time_stamp) {
        this.text = text;
        this.time_stamp = time_stamp;
        this.uid = uid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Map<String, String> getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Map<String, String> time_stamp) {
        this.time_stamp = time_stamp;
    }
}
