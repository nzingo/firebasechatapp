package com.boutik.nadhir.firebasechatapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.models.MessageModel;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesViewHolder>{
    private List<MessageModel> messages_list;
    String my_uid;


    public MessagesAdapter(List<MessageModel> messages_list, String my_uid ) {
        this.messages_list = messages_list;
        this.my_uid = my_uid;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_message_card, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.other_message_card, parent, false);
        }

        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.setText(messages_list.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return messages_list.size();
    }

    @Override
    public int getItemViewType(int position) {

        String uid = messages_list.get(position).getUid();
        if(uid.equals(my_uid)){
            return 1;
        }else {
            return 2;
        }
    }
}
