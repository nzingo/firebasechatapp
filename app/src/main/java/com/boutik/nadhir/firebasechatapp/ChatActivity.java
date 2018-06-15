package com.boutik.nadhir.firebasechatapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.adapters.MessagesAdapter;
import com.boutik.nadhir.firebasechatapp.adapters.ThreadsAdapter;
import com.boutik.nadhir.firebasechatapp.models.MessageModel;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String thread_key;
    private String receiver_id;
    private String my_id;
    private String user_name;

    private DatabaseReference messagesRef;
    private DatabaseReference threadsRef;
    private DatabaseReference usersRef;

    private boolean first_time = true;
    private EditText editText;
    private MessageModel msg;

    private RecyclerView mRecyclerView;
    private final List<MessageModel> messages_list = new ArrayList<>();
    private MessagesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user_name = getIntent().getStringExtra("user_name");
        getSupportActionBar().setTitle(user_name);

        receiver_id = getIntent().getStringExtra("uid");
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
        threadsRef = FirebaseDatabase.getInstance().getReference().child("threads");
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        editText=findViewById(R.id.editWriteMessage);
        ImageButton send_button = findViewById(R.id.btnSend);

        mRecyclerView = findViewById(R.id.recyclerChat);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessagesAdapter(messages_list, my_id);
        mRecyclerView.setAdapter(mAdapter);

        thread_key = getIntent().getStringExtra("th_key");

        if (thread_key == null){
            thread_key = messagesRef.push().getKey();
        }

        load_messages();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editText.length()>0){
                   String message_text = editText.getText().toString();
                   msg = new MessageModel( my_id, message_text, ServerValue.TIMESTAMP);
                   mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                   if(first_time){

                       initThread();   /************     needs to be atomic     **********/
                       sendMessage();   //
                       first_time=false;    //

                   }else{

                       sendMessage();   /************    (nop) needs to be atomic     **********/
                       updateThread();  //

                   }
                   editText.setText("");
               }
            }
        });

    }



    public void initThread(){

        /*********************************  set name and image from uid ****************************************/

        //thread_key = messagesRef.push().getKey();

        ThreadModel thread_sender = new ThreadModel(my_id,"sheard pref","sheard pref url", msg.getText(), msg.getTime_stamp());
        ThreadModel thread_receiver = new ThreadModel(receiver_id,"item detail","item detail", msg.getText(), msg.getTime_stamp());
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + receiver_id + "/" + thread_key, thread_sender);
        childUpdates.put("/" + my_id + "/" + thread_key, thread_receiver);
        threadsRef.updateChildren(childUpdates);
    }

    public void sendMessage(){
        String message_key = messagesRef.child(thread_key).push().getKey();
        messagesRef.child(thread_key).child(message_key).setValue(msg);

        /*************         message sent succesfully        ***************/
        //  set on complete listener
    }

    /********************updateThread() timestamp and last message in both users threads******************/
    public void updateThread(){

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/last_message", msg.getText());
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/time_stamp", msg.getTime_stamp());
        childUpdates.put("/" + my_id + "/" + thread_key + "/last_message", msg.getText());
        childUpdates.put("/" + my_id + "/" + thread_key + "/time_stamp", msg.getTime_stamp());

        threadsRef.updateChildren(childUpdates);
    }

    private void load_messages() {

        DatabaseReference myThreadRef = messagesRef.child(thread_key);
        myThreadRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                messages_list.add(message);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
