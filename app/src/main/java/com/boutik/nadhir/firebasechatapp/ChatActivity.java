package com.boutik.nadhir.firebasechatapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.adapters.MessagesAdapter;
import com.boutik.nadhir.firebasechatapp.adapters.ThreadsAdapter;
import com.boutik.nadhir.firebasechatapp.models.MessageModel;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private DatabaseReference myThreadRef;
    private ChildEventListener listener;

    private boolean is_empty ;
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

        mRecyclerView = findViewById(R.id.recyclerChat);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MessagesAdapter(messages_list, my_id);
        mRecyclerView.setAdapter(mAdapter);

        editText=findViewById(R.id.editWriteMessage);

        thread_key = getIntent().getStringExtra("th_key");

        is_empty = false;
        if (thread_key == null){
            is_empty = true;
            thread_key = messagesRef.push().getKey();
        }

        myThreadRef = messagesRef.child(thread_key);

        init_listener();

        ImageButton send_button = findViewById(R.id.btnSend);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editText.length()>0){
                   String message_text = editText.getText().toString();
                   msg = new MessageModel( my_id, message_text);
                   if(is_empty){
                       createThread();
                       sendMessage();
                       is_empty=false;
                   }else{
                       sendMessage();
                       //updateThread(); //its ok to update each time we send msg
                   }
                   editText.setText("");
               }
            }
        });

    }

    public void createThread(){

        /*********************************  set name and image from uid ****************************************/

        //thread_key = messagesRef.push().getKey();

//        ThreadModel thread_sender = new ThreadModel(my_id,"sheard pref","sheard pref url", msg.getText(),false);
//        ThreadModel thread_receiver = new ThreadModel(receiver_id,"item detail","item detail", msg.getText(),false);

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/" + receiver_id + "/" + thread_key + "/uid", my_id);
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/name", "sheard pref");
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/profile_img", "sheard pref url");
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/last_message", msg.getText());
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/item_title", "asking about item");
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/time_stamp", ServerValue.TIMESTAMP);
        childUpdates.put("/" + receiver_id + "/" + thread_key + "/seen", false);


        childUpdates.put("/" + my_id + "/" + thread_key + "/uid", receiver_id);
        childUpdates.put("/" + my_id + "/" + thread_key + "/name", "item detail");
        childUpdates.put("/" + my_id + "/" + thread_key + "/profile_img", "item detail");
        childUpdates.put("/" + my_id + "/" + thread_key + "/last_message", msg.getText());
        childUpdates.put("/" + my_id + "/" + thread_key + "/item_title", "asking about item");
        childUpdates.put("/" + my_id + "/" + thread_key + "/time_stamp", ServerValue.TIMESTAMP);
        childUpdates.put("/" + my_id + "/" + thread_key + "/seen", false);

        threadsRef.updateChildren(childUpdates);
    }

    public void sendMessage(){

        String message_key = messagesRef.child(thread_key).push().getKey();

        messagesRef.child(thread_key).child(message_key).setValue(msg);

        /***************************       test downloads       ***************************/
        //threadsRef.child(receiver_id).child(thread_key).child("seen").setValue(false);
        /***************************       test downloads       ***************************/
    }

    public void updateThread(){

        Map<String, Object> childUpdates = new HashMap<>();

        if(msg != null) {
            childUpdates.put("/" + receiver_id + "/" + thread_key + "/last_message", msg.getText());
            childUpdates.put("/" + receiver_id + "/" + thread_key + "/time_stamp", ServerValue.TIMESTAMP);
            childUpdates.put("/" + receiver_id + "/" + thread_key + "/seen", false);

            childUpdates.put("/" + my_id + "/" + thread_key + "/last_message", msg.getText());
            childUpdates.put("/" + my_id + "/" + thread_key + "/time_stamp", ServerValue.TIMESTAMP);
        }
        if(!is_empty){
            childUpdates.put("/" + my_id + "/" + thread_key + "/seen", true);
        }
        threadsRef.updateChildren(childUpdates);
    }

    private void init_listener() {

        listener = new ChildEventListener() {
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
        };

        //myThreadRef.addChildEventListener(listener);

        }

    @Override
    protected void onStop() {
        super.onStop();
            updateThread();
//        if(!is_empty){
//            threadsRef.child(my_id).child(thread_key).child("seen").setValue(true);
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myThreadRef.removeEventListener(listener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages_list.clear();
        myThreadRef.addChildEventListener(listener);
    }
}
