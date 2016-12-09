package com.example.duclinh.appchat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.socketio.client.IO;

import java.net.InetAddress;
import java.net.URISyntaxException;

public class Activity_Splash extends AppCompatActivity {
    private AppCompatButton start;
    private EditText ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__splash);
        start = (AppCompatButton) findViewById(R.id.activity_splash_start);
        ip = (EditText) findViewById(R.id.activity_splash_ip);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip_address = ip.getText().toString();
                if(ip_address.equals("")){
                    Toast.makeText(Activity_Splash.this, "Chưa nhập địa chỉ máy chủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!isNetworkConnected()){
                    Toast.makeText(Activity_Splash.this, "Không có kết nối internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyApplication.HOST = ip_address;
                try {
                    MyApplication.socket = IO.socket(MyApplication.HOST);
                    Intent intent = new Intent(Activity_Splash.this, MainActivity.class);
                    Activity_Splash.this.startActivity(intent);
                    Activity_Splash.this.finish();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com"); //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }

    }
}
