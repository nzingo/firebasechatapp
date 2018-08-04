package com.boutik.nadhir.firebasechatapp.adapters;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.ChatActivity;
import com.boutik.nadhir.firebasechatapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThreadsViewHolder extends RecyclerView.ViewHolder {
    public View mView;

    public ThreadsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView userNameView = mView.findViewById(R.id.display_name);
        userNameView.setText(name);
    }
    public void setMessage(String name) {
        TextView messageView = mView.findViewById(R.id.display_message);
        messageView.setText(name);
    }
    public void setOnline(boolean isOnline){
        if(!isOnline){
            View online =  mView.findViewById(R.id.online);
            online.setVisibility(View.INVISIBLE);
        }
    }
    public void setTime(String t){
        TextView time = mView.findViewById(R.id.timestamp);
        time.setText(t);
    }
    public void setTitle(String title){
        TextView item_title = mView.findViewById(R.id.display_item_title);
        item_title.setText(title);
    }
    public void setSeen(boolean seen){
        TextView userNameView = mView.findViewById(R.id.display_name);
        TextView message = mView.findViewById(R.id.display_message);
        TextView title = mView.findViewById(R.id.display_item_title);
        if(seen){
            message.setTypeface(null);
            userNameView.setTypeface(null);
            title.setTypeface(null);

        }else {
            message.setTypeface(null, Typeface.BOLD);
            userNameView.setTypeface(null,Typeface.BOLD);
            title.setTypeface(null,Typeface.BOLD);
        }
    }

}
