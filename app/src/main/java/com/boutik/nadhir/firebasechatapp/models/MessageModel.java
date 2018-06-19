package com.boutik.nadhir.firebasechatapp.models;

import java.util.Map;

public class MessageModel {
    private String text;
    private String uid;

    public MessageModel() {
    }

    public MessageModel(String uid, String text) {
        this.text = text;
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
}
