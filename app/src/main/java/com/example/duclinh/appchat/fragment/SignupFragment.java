package com.example.duclinh.appchat.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by haixo on 8/18/2016.
 */
public class SignupFragment extends DialogFragment {
    private Context context;
    private EditText account;
    private EditText password;
    private EditText rePassword;
    private AppCompatButton createAccount;
    private TextView alreadyAccount;

    public SignupFragment(){

    }

    public static SignupFragment newInstance(String title){
        SignupFragment signupFragment = new SignupFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        signupFragment.setArguments(args);
        return signupFragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlView(view);
        controlEvent();
    }

    private void controlEvent() {

        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MyApplication.socket.connected()){
                    MyApplication.socket.connect();
                }
                final ProgressDialog progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Authenticating...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                final String textAccoun = account.getText().toString();
                final String textPassword = password.getText().toString();
                String textRePassword = rePassword.getText().toString();
                if(textPassword.equals("")||textPassword.equals("")||textRePassword.equals("")){
                    Toast.makeText(context, "Chưa nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!textPassword.equals(textRePassword)){
                    Toast.makeText(context, "Xác nhận mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("account", textAccoun);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonObject.put("password", textPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication.socket.emit("signUp", jsonObject);
                MyApplication.socket.once("resultSignUp", new Emitter.Listener() {
                    @Override
                    public void call(final Object... args) {
                        progressDialog.dismiss();
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                JSONObject data = (JSONObject) args[0];
                                int errorCode = 0;
                                try {
                                    errorCode = data.getInt("status");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(errorCode == 100){
                                    // when sigup successfull
                                    Toast.makeText(context, "Tao tai khoan thanh cong", Toast.LENGTH_SHORT).show();
                                }
                                else if (errorCode == 101){
                                    /// when account already exist
                                    Toast.makeText(context, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                                }
                                else if(errorCode == 102){
                                    /// when email error
                                    Toast.makeText(context, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    // when app error
                                    Toast.makeText(context, "Hệ thống bị lỗi", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }
                });


            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
    }

    private void controlView(View view) {
        account = (EditText) view.findViewById(R.id.fragment_signup_account);
        password = (EditText) view.findViewById(R.id.fragment_signup_password);
        rePassword = (EditText) view.findViewById(R.id.fragment_signup_repassword);
        createAccount = (AppCompatButton) view.findViewById(R.id.fragment_signup_createaccount);
        alreadyAccount = (TextView) view.findViewById(R.id.fragment_signup_alreadyaccount);
    }
}
