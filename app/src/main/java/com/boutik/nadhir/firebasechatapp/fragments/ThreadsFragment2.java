package com.boutik.nadhir.firebasechatapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boutik.nadhir.firebasechatapp.ChatActivity;
import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.adapters.ThreadsViewHolder;
import com.boutik.nadhir.firebasechatapp.adapters.UsersViewHolder;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.boutik.nadhir.firebasechatapp.models.UserModel;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ThreadsFragment2 extends Fragment {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter<ThreadModel, ThreadsViewHolder> firebaseRecyclerAdapter ;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference threadsRef;
    Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        threadsRef = FirebaseDatabase.getInstance().getReference().child("threads").child(currentUser.getUid());

        query = threadsRef.orderByChild("time_stamp");

        FirebaseRecyclerOptions threadsOptions = new FirebaseRecyclerOptions.Builder<ThreadModel>()
                .setQuery(query, ThreadModel.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ThreadModel, ThreadsViewHolder>(threadsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ThreadsViewHolder holder, int position, @NonNull ThreadModel model) {

                holder.setMessage(model.getLast_message());
                holder.setName(model.getName());
                holder.setTitle(model.getItem_title());
                holder.setOnline(false);
                holder.setSeen(model.isSeen());
                holder.setTime(to_time(model.getTime_stamp()));
//
                final String thread_key = getRef(position).getKey();
                final String uid = model.getUid();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),ChatActivity.class);
                        intent.putExtra("th_key",thread_key);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                    }
                });
                //mRecyclerView.smoothScrollToPosition(position);
            }

            @NonNull
            @Override
            public ThreadsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.thread_card, parent, false);
                //mRecyclerView.scrollToPosition(firebaseRecyclerAdapter.getItemCount()-1);
                return new ThreadsViewHolder(view);
            }
        };

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView = view.findViewById(R.id.threads_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //firebaseRecyclerAdapter.onChildChanged(ChangeEventType.ADDED,firebaseRecyclerAdapter.getSnapshots(),);
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        return view;
    }

    private String to_time(long timestamp) {

        long oneWeek = 7*24*60*60*1000;
        long oneDay = 24*60*60*1000;

        Date time = new Date(timestamp);

        long dayAgo = System.currentTimeMillis()-oneDay;
        long weekAgo = System.currentTimeMillis()-oneWeek;
        Date dayAgoDate = new Date(dayAgo);
        Date weekAgoDate = new Date(weekAgo);

        int flags;

        if(compareToDay(time,dayAgoDate)>0) {
            flags = DateUtils.FORMAT_SHOW_TIME;
        }else if(compareToDay(time,weekAgoDate)>0){
            flags = DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY ;
        }else {
            flags = DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_SHOW_DATE ;
        }

        String ago = DateUtils.formatDateTime(getActivity(), timestamp, flags);
        //Log.i("time_ago",ago.toString());
        return ago;
    }

    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return sdf.format(date1).compareTo(sdf.format(date2));
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

//    @Override
//    public void onResume() {
//        super.onResume();
////        if(mRecyclerView!=null){
////            mRecyclerView.smoothScrollToPosition(firebaseRecyclerAdapter.getItemCount());
////        }
//    }
}
