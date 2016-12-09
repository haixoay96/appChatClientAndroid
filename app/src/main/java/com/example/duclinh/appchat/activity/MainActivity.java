package com.example.duclinh.appchat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.fragment.ForgetPasswordFragment;
import com.example.duclinh.appchat.fragment.SignupFragment;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText account;
    private EditText password;
    private AppCompatButton login;
    private TextView createAccount;
    private TextView forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlView();
        controlLogic();
        controlEvent();
    }

    private void controlLogic() {

    }

    private void controlEvent() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MyApplication.socket.connected()) {
                    MyApplication.socket.connect();
                }
                final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.setCancelable(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final String textAccount = account.getText().toString();
                final String textPassword = password.getText().toString();
                final JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("username", textAccount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("password", textPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication.socket.emit("login", jsonObject);
                MyApplication.socket.off("resultLogin");
                MyApplication.socket.once("resultLogin", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        progressDialog.dismiss();
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONObject jsonObject = (JSONObject) args[0];
                                    int statusCode = 0;
                                    statusCode = jsonObject.getInt("statusCode");
                                    Log.d("code", statusCode+"");
                                    if (statusCode == 100) {
                                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, UsersOnlineActivity.class);
                                        intent.putExtra("username", textAccount);
                                        intent.putExtra("password", textPassword);
                                        intent.putExtra("nickname", jsonObject.getString("nickname"));
                                        intent.putExtra("avatar", jsonObject.getString("avatar"));
                                        startActivity(intent);
                                        finish();
                                    } else if (statusCode == 101) {
                                        Toast.makeText(MainActivity.this, "Bạn socket đã được sử dụng", Toast.LENGTH_SHORT).show();

                                    } else if(statusCode == 102){
                                        //case eles
                                        Toast.makeText(MainActivity.this, "Lỗi hệ thống", Toast.LENGTH_SHORT).show();

                                    }else if(statusCode == 103){
                                        Toast.makeText(MainActivity.this, "Tài khoản này đã được đăng nhập", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(MainActivity.this, "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }
                });
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                SignupFragment fragment = SignupFragment.newInstance("Create account");
                fragment.show(fragmentManager, "fragment");
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                ForgetPasswordFragment fragment = ForgetPasswordFragment.newInstance("Rest password");
                fragment.show(fragmentManager, "fragment");
            }
        });


    }

    private void controlView() {
        account = (EditText) findViewById(R.id.activity_login_account);
        password = (EditText) findViewById(R.id.activity_login_password);
        login = (AppCompatButton) findViewById(R.id.activity_login_login);
        createAccount = (TextView) findViewById(R.id.activity_login_createaccount);
        forgetPassword = (TextView) findViewById(R.id.activity_login_forgetpassword);
    }
}
