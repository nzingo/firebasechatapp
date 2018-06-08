package com.boutik.nadhir.firebasechatapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class UsersViewHolder extends RecyclerView.ViewHolder{
    View mView;

    public UsersViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setName(String name) {
        TextView userNameView = mView.findViewById(R.id.display_name);
        userNameView.setText(name);
    }
    public void setEmail(String name) {
        TextView userEmailView = mView.findViewById(R.id.display_email);
        userEmailView.setText(name);
    }
    public void setOnline(boolean isOnline){
        if(!isOnline){
            View online =  mView.findViewById(R.id.online);
            online.setVisibility(View.INVISIBLE);
        }
    }
}
