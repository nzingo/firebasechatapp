package com.boutik.nadhir.firebasechatapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.R;

public class MessagesViewHolder extends RecyclerView.ViewHolder{
    View mView;


    public MessagesViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setText(String name) {
        TextView messageTextView = mView.findViewById(R.id.display_message_text);
        messageTextView.setText(name);
    }

}
