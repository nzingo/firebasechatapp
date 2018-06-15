package com.boutik.nadhir.firebasechatapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TimingLogger;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.boutik.nadhir.firebasechatapp.ChatActivity;
import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.adapters.UsersViewHolder;
import com.boutik.nadhir.firebasechatapp.models.UserModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UsersFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference usersRef;
    FirebaseRecyclerAdapter<UserModel,UsersViewHolder> firebaseRecyclerAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        mRecyclerView = view.findViewById(R.id.users_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        /******************************************************************************************************/
        TimingLogger timings = new TimingLogger("ffs","database reference");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");        //time consoming!!!!!!!!!!!!!!!!!!!
        timings.addSplit("work A");
        timings.dumpToLog();
        /*****************************************************************************************************/


        //Query usersQuery = usersRef.orderByKey();
        FirebaseRecyclerOptions usersOptions = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(usersRef, UserModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<UserModel, UsersViewHolder>(usersOptions) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder holder, int position, @NonNull UserModel model) {
                holder.setName(model.getUsername());
                holder.setEmail(model.getEmail());
                holder.setOnline(model.isOnline());


                final String uid = getRef(position).getKey();
                final String user_name = model.getUsername();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),ChatActivity.class);
                        intent.putExtra("uid",uid);
                        intent.putExtra("user_name",user_name);
                        startActivity(intent);

                    }
                });

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_card, parent, false);

                return new UsersViewHolder(view);
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }
}
