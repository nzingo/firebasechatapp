package com.boutik.nadhir.firebasechatapp.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.adapters.ThreadsAdapter;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private List<ThreadModel> thread_list = new ArrayList<>();//final
    private List<String> key_list = new ArrayList<String>();
    private ThreadsAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        mRecyclerView = view.findViewById(R.id.threads_recycler);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setStackFromEnd(true);
        llm.setReverseLayout(true);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new ThreadsAdapter(getActivity(), thread_list, key_list);
        mRecyclerView.setAdapter(mAdapter);

        load_threads();

        return view;
    }

    private void load_threads() {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference threadsRef = FirebaseDatabase.getInstance().getReference().child("threads").child(currentUser.getUid());
        Query query = threadsRef.orderByChild("time_stamp");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ThreadModel thread = dataSnapshot.getValue(ThreadModel.class);
                thread_list.add(thread);
                key_list.add(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                int pos = key_list.indexOf(dataSnapshot.getKey());
                ThreadModel thread = dataSnapshot.getValue(ThreadModel.class);
                thread_list.remove(pos);
                //key_list.remove(pos);
                thread_list.add(pos,thread);
                //key_list.add(dataSnapshot.getKey());
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());

                // use hash map <String thred_key , threadModel>
                //  hashMap[dataSnapshot.getKey] = dataSnapshot.getValue()

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int pos = key_list.indexOf(dataSnapshot.getKey());
                thread_list.remove(pos);
                key_list.remove(pos);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
