package com.boutik.nadhir.firebasechatapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.models.UserModel;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder>{
    private List<UserModel> users_list;

    public UsersAdapter(List<UserModel> users_list) {
        this.users_list = users_list;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {

        holder.setName(users_list.get(position).getUsername());
        holder.setEmail(users_list.get(position).getEmail());
        holder.setOnline(users_list.get(position).isOnline());


    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }
}
