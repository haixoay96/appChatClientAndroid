package com.example.duclinh.appchat.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.adapter.AdapterListMessageChat;
import com.example.duclinh.appchat.data.FormMessage;
import com.example.duclinh.appchat.designnotifi.receivermessage.CenterManager;
import com.example.duclinh.appchat.designnotifi.receivermessage.Client;
import com.example.duclinh.appchat.orther.MyApplication;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by haixo on 8/19/2016.
 */
public class ScreenChatFragment extends DialogFragment implements Client{
    private Context context;
    private RecyclerView listMessage;
    private EditText message;
    private CenterManager centerManagerMessage;
    private AppCompatButton send;
    private TextView accountTitle;
    private ArrayList<FormMessage> listData;
    private AdapterListMessageChat adapterListMessageChat;
    private RecyclerView.LayoutManager layoutManager;
    private String account;



    public ScreenChatFragment(){

    }
    public ScreenChatFragment(CenterManager centerManagerMessage , ArrayList<FormMessage> listData){
        this.centerManagerMessage = centerManagerMessage;
        this.listData = listData;
    }
    public static ScreenChatFragment newInstance(String account, CenterManager centerManagerMessage, ArrayList<FormMessage> listData){
        ScreenChatFragment screenChatFragment = new ScreenChatFragment(centerManagerMessage, listData);
        Bundle args = new Bundle();
        args.putString("account", account);
        screenChatFragment.setArguments(args);
        return screenChatFragment;
    }
    @Override
    public void update(String account, String message, int sender) {
        adapterListMessageChat.notifyDataSetChanged();
        listMessage.scrollToPosition(listData.size()-1);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_screen_chat,container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        controlView(view);
        controlLogic();
        controlEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        centerManagerMessage.removeClient(this);
        Toast.makeText(context, "remove", Toast.LENGTH_SHORT).show();
    }

    private void controlLogic() {
        account =getArguments().getString("account");
        getDialog().setTitle(account);
        accountTitle.setText(account);
        adapterListMessageChat = new AdapterListMessageChat(listData);
        layoutManager = new LinearLayoutManager(context);
        //((LinearLayoutManager)layoutManager).setReverseLayout(true);
        listMessage.setLayoutManager(layoutManager);
        listMessage.setAdapter(adapterListMessageChat);
        adapterListMessageChat.notifyDataSetChanged();
        listMessage.scrollToPosition(listData.size() -1 );
        centerManagerMessage.addClient(this);
    }
    private void controlView(View view) {
        listMessage = (RecyclerView) view.findViewById(R.id.fragment_screen_chat_listmessage);
        message = (EditText) view.findViewById(R.id.fragment_screen_chat_message);
        send = (AppCompatButton) view.findViewById(R.id.fragment_screen_chat_send);
        accountTitle = (TextView) view.findViewById(R.id.fragment_screen_chat_accounttitle);
    }
    private void controlEvent() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object = new JSONObject();
                final String data = message.getText().toString();
                message.setText("");
                try {
                    object.put("account", account);
                    object.put("message", data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MyApplication.socket.emit("sendMessage",object);
            }
        });

    }

}
