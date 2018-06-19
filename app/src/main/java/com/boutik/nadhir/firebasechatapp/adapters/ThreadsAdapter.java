package com.boutik.nadhir.firebasechatapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.ChatActivity;
import com.boutik.nadhir.firebasechatapp.R;
import com.boutik.nadhir.firebasechatapp.models.MessageModel;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsAdapter.ThreadsViewHolder>/* implements View.OnClickListener */{
    private List<ThreadModel> thread_list;
    private List<String> key_list ;
    private String thread_key;
    private Context context;

    public ThreadsAdapter(Context context, List<ThreadModel> thread_list, List<String> key_list) {

        this.key_list = key_list;
        this.thread_list = thread_list;
        this.context = context;
    }


    public class ThreadsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;

        public ThreadsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mView.setOnClickListener(this);
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
        public void setTime(long timestamp){
            TextView time = mView.findViewById(R.id.timestamp);
            time.setText(to_time(timestamp));
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
                //userNameView.setTextColor(Color.rgb(105,105,105));
                //message.setTextColor(Color.GRAY);
                message.setTypeface(null);
                userNameView.setTypeface(null);
                title.setTypeface(null);
               // title.setTextColor(Color.GRAY);

            }else {
                //userNameView.setTextColor(Color.BLACK);
               // message.setTextColor(Color.BLACK);
                message.setTypeface(null,Typeface.BOLD);
                userNameView.setTypeface(null,Typeface.BOLD);
                title.setTypeface(null,Typeface.BOLD);
                //title.setTextColor(Color.BLACK);
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context,ChatActivity.class);
            thread_key = key_list.get(getLayoutPosition());
            intent.putExtra("uid",thread_list.get(getLayoutPosition()).getUid());
            intent.putExtra("th_key",thread_key);
            context.startActivity(intent);
        }
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
        holder.setTime(thread_list.get(position).getTime_stamp());
        holder.setSeen(thread_list.get(position).isSeen());
        holder.setTitle(thread_list.get(position).getItem_title());
    }

    @Override
    public int getItemCount() {
        return thread_list.size();
    }

    private CharSequence to_time(long timestamp) {

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

        String ago = DateUtils.formatDateTime(context, timestamp, flags);
        //Log.i("time_ago",ago.toString());
        return ago;
    }

    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }

}
