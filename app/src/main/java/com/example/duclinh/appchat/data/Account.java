package com.example.duclinh.appchat.data;

import com.example.duclinh.appchat.designnotifi.receivermessage.Client;
import com.example.duclinh.appchat.orther.MyApplication;

import java.util.ArrayList;

/**
 * Created by haixo on 8/26/2016.
 */
public class Account implements Client {
    private String username;
    private String nickname;
    private String avatar;
    private String status;
    private ArrayList<FormMessage> list;

    public Account(String username, String nickname, String avatar, String status) {
        this.username = username;
        this.nickname = nickname;
        this.avatar = avatar;
        this.status = status;
        list = new ArrayList<FormMessage>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setList(ArrayList<FormMessage> list) {
        this.list = list;
    }

    public ArrayList<FormMessage> getList() {
        return list;
    }

    @Override
    public void update(String username, String message, int sender) {
        if (this.username.equals(username)) {
            list.add(new FormMessage(MyApplication.HOST + avatar, message, sender));
        }
    }
}
