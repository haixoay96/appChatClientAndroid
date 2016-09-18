package com.example.duclinh.appchat.designnotifi.receivermessage;

/**
 * Created by haixo on 8/21/2016.
 */
public interface Observer {
    void notifiAllClient(String account , String message, int sender);
    void addClient(Client client);
    void removeClient(Client client);
}
