package com.example.duclinh.appchat.data;

/**
 * Created by haixo on 8/19/2016.
 */
public class FormMessage {
    private String avatar;
    private String message;
    private int sender;

    public FormMessage(String avatar, String message , int sender) {
        this.message = message;
        this.avatar = avatar;
        this.sender = sender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSender() {
        return sender;
    }

    public void setSender(int sender) {
        this.sender = sender;
    }
}
