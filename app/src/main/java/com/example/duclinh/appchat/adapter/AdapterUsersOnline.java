package com.example.duclinh.appchat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.data.Account;
import com.example.duclinh.appchat.orther.MyApplication;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haixo on 8/13/2016.
 */
public class AdapterUsersOnline extends RecyclerView.Adapter<AdapterUsersOnline.MyViewHolder> {
    private ArrayList<Account> list;
    private ViewGroup parent;

    public AdapterUsersOnline(ArrayList<Account> list) {
        this.list = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView avatar;
        private TextView account;
        private TextView status;

        public MyViewHolder(View view) {
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.item_users_online_avatar);
            account = (TextView) view.findViewById(R.id.item_users_online_account);
            status = (TextView) view.findViewById(R.id.item_users_online_status);
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }

        public CircleImageView getAvatar() {
            return avatar;
        }

        public void setAvatar(CircleImageView avatar) {
            this.avatar = avatar;
        }

        public TextView getAccount() {
            return account;
        }

        public void setAccountt(TextView nickName) {
            this.account = nickName;
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_users_online, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Account account = list.get(position);
        Picasso.with(parent.getContext()).load(MyApplication.HOST+account.getAvatar()).resize(500,500).into(holder.getAvatar());
        holder.getAccount().setText(account.getNameAccount());
        holder.getStatus().setText("Cuoi len");
        Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(parent.getContext(), R.anim.anticipateovershoot_interpolator);
        holder.itemView.setAnimation(animAnticipateOvershoot);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


}
