package com.example.duclinh.appchat.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duclinh.appchat.R;
import com.example.duclinh.appchat.data.FormMessage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by haixo on 8/19/2016.
 */
public class AdapterListMessageChat extends RecyclerView.Adapter<AdapterListMessageChat.MyViewHolder>{
    private ViewGroup parent;
    private ArrayList<FormMessage> listData;

    public AdapterListMessageChat(ArrayList<FormMessage> listData){
        this.listData = listData;

    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView avatar;
        private TextView message;
        private CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            avatar = (CircleImageView) view.findViewById(R.id.item_message_chat_avatar);
            message = (TextView) view.findViewById(R.id.item_message_chat_message);
            cardView = (CardView) view.findViewById(R.id.item_message_chat_cardview);
        }

        public CardView getCardView() {
            return cardView;
        }

        public void setCardView(CardView cardView) {
            this.cardView = cardView;
        }

        public CircleImageView getAvatar() {
            return avatar;
        }

        public void setAvatar(CircleImageView avatar) {
            this.avatar = avatar;
        }

        public TextView getMessage() {
            return message;
        }

        public void setMessage(TextView message) {
            this.message = message;
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_chat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FormMessage formMessage = listData.get(position);
        CircleImageView avatar = holder.getAvatar();
        TextView message = holder.getMessage();
        CardView cardView = holder.getCardView();
        Picasso.with(parent.getContext()).load(formMessage.getAvatar()).resize(500,500).into(avatar);
        message.setText(formMessage.getMessage());
        if(formMessage.getSender()==1){
            ((RelativeLayout.LayoutParams)avatar.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            ((RelativeLayout.LayoutParams)cardView.getLayoutParams()).removeRule(RelativeLayout.RIGHT_OF);
            ((RelativeLayout.LayoutParams)avatar.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
            ((RelativeLayout.LayoutParams)cardView.getLayoutParams()).addRule(RelativeLayout.LEFT_OF,R.id.item_message_chat_avatar);
        }
        else {
            ((RelativeLayout.LayoutParams)avatar.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            ((RelativeLayout.LayoutParams)cardView.getLayoutParams()).removeRule(RelativeLayout.LEFT_OF);
            ((RelativeLayout.LayoutParams)avatar.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
            ((RelativeLayout.LayoutParams)cardView.getLayoutParams()).addRule(RelativeLayout.RIGHT_OF,R.id.item_message_chat_avatar);
        }

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
