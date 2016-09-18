package com.example.duclinh.appchat.orther;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by duclinh on 07/08/2016.
 */
public class MyApplication extends Application {
    public static final String HOST = "http://192.168.1.27:3000";
    public static Socket socket = null;

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            MyApplication.socket = IO.socket(MyApplication.HOST);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
