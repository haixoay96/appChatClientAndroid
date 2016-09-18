package com.example.duclinh.appchat.designnotifi.receivermessage;

import java.util.ArrayList;

/**
 * Created by haixo on 8/21/2016.
 */
public class CenterManager implements Observer {
    private ArrayList<Client> listClient;
    public CenterManager(){
        listClient = new ArrayList<Client>();
    }
    @Override
    public void addClient(Client client){
        listClient.add(client);
    }

    @Override
    public void removeClient(Client client) {
        listClient.remove(client);
    }
    @Override
    public void notifiAllClient(String account, String message, int sender) {
        for (Client client:listClient) {
            client.update(account, message, sender);
        }
    }
}
