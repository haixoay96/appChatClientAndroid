package com.example.duclinh.appchat.orther;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by duclinh on 07/08/2016.
 */
public class MyApplication extends Application {
    public static String HOST = null;
    public static Socket socket = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
