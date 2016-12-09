package com.example.duclinh.appchat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by duclinh on 06/12/2016.
 */

public class ChangePasswordFragment extends DialogFragment {
    private EditText oldpassword;
    private EditText password;
    private EditText repassword;
    private AppCompatButton change;
    private Context context;

    public ChangePasswordFragment() {

    }

    public static ChangePasswordFragment newInstance(String title) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlView(view);
        controlEvent();

    }

    private void controlEvent() {
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final JSONObject object = new JSONObject();
                String old = oldpassword.getText().toString();
                String pass = password.getText().toString();
                final String re = repassword.getText().toString();
                if (old.equals("") || pass.equals("") || re.equals("")) {
                    Toast.makeText(context, "Nhập thiếu thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(re)) {
                    Toast.makeText(context, "Repassword không đúng", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    object.put("oldpassword", old);
                    object.put("password", pass);
                    MyApplication.socket.emit("changePassword", object);
                    MyApplication.socket.once("resultChangePassword", new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            JSONObject object1 = (JSONObject) args[0];
                            try {
                                int statusCode = object1.getInt("status");
                                if (statusCode == 100) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, "Change password thành công", Toast.LENGTH_SHORT).show();
                                            dismiss();

                                        }
                                    });
                                    return;
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "Không thành công!", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void controlView(View view) {
        oldpassword = (EditText) view.findViewById(R.id.fragmnet_change_password_old_password);
        password = (EditText) view.findViewById(R.id.fragmnet_change_password_password);
        repassword = (EditText) view.findViewById(R.id.fragmnet_change_password_repassword);
        change = (AppCompatButton) view.findViewById(R.id.fragmnet_change_password_change);
    }


    @Override
    public void onResume() {
        super.onResume();

        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);

    }
}
