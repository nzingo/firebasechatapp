package com.boutik.nadhir.firebasechatapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.R;


public class ThreadsViewHolder extends RecyclerView.ViewHolder{
    View mView;


    public ThreadsViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView userNameView = mView.findViewById(R.id.display_name);
        userNameView.setText(name);
    }
    public void setMessage(String name) {
        TextView userEmailView = mView.findViewById(R.id.display_message);
        userEmailView.setText(name);
    }
    public void setOnline(boolean isOnline){
        if(!isOnline){
            View online =  mView.findViewById(R.id.online);
            online.setVisibility(View.INVISIBLE);
        }
    }
}
