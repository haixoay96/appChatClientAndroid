package com.example.duclinh.appchat.data;

import com.example.duclinh.appchat.designnotifi.receivermessage.Client;
import com.example.duclinh.appchat.orther.MyApplication;

import java.util.ArrayList;

/**
 * Created by haixo on 8/26/2016.
 */
public class Account implements Client {
    private String nameAccount;
    private String avatar;
    private ArrayList<FormMessage> list;

    public Account(String nameAccount, String avatar) {
        this.nameAccount = nameAccount;
        this.avatar = avatar;
        list = new ArrayList<FormMessage>();
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public ArrayList<FormMessage> getList() {
        return list;
    }

    public void setList(ArrayList<FormMessage> list) {
        this.list = list;
    }

    @Override
    public void update(String account, String message, int sender) {
        if(nameAccount.equals(account)){
            list.add(new FormMessage(MyApplication.HOST+"/data/avatar/defaultavatar.jpg", message, sender));
        }
    }
}
