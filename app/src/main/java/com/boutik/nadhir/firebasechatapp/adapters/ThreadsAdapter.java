package com.boutik.nadhir.firebasechatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boutik.nadhir.firebasechatapp.ChatActivity;
import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.models.MessageModel;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsViewHolder>{
    private List<ThreadModel> thread_list;
    private List<String> key_list ;
    private String thread_key;
    private Context context;
    //private DatabaseReference usersRef;

    public ThreadsAdapter(Context context, List<ThreadModel> thread_list, List<String> key_list) {

        this.key_list = key_list;
        this.thread_list = thread_list;
        this.context = context;
        //usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    }

    @NonNull
    @Override
    public ThreadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.thread_card, parent, false);

        return new ThreadsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadsViewHolder holder, int position) {


        holder.setName(thread_list.get(position).getName());
        holder.setMessage(thread_list.get(position).getLast_message());
        holder.setOnline(false);

        thread_key = key_list.get(position);

        holder.mView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("th_key",thread_key);
                context.startActivity(intent);

            }
        }));

    }

    @Override
    public int getItemCount() {
        return thread_list.size();
    }
}
