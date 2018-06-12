package com.boutik.nadhir.firebasechatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.boutik.nadhir.firebasechatapp.models.MessageModel;
import com.boutik.nadhir.firebasechatapp.models.ThreadModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private String thread_key;
    private String receiver_id;
    private String my_id;

    private DatabaseReference messagesRef;
    private DatabaseReference threadsRef;
    private boolean first_time = true;
    private EditText editText;
    private MessageModel msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String name = getIntent().getStringExtra("user_name");
        getSupportActionBar().setTitle(name);

        receiver_id = getIntent().getStringExtra("uid");
        my_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messagesRef = FirebaseDatabase.getInstance().getReference().child("messages");
        threadsRef = FirebaseDatabase.getInstance().getReference().child("threads");

        editText=findViewById(R.id.editWriteMessage);
        ImageButton send_button = findViewById(R.id.btnSend);

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editText.length()>0){
                   String message_text = editText.getText().toString();
                   msg = new MessageModel( my_id, message_text, ServerValue.TIMESTAMP);

                   if(first_time){

                       initThread();   /************     needs to be atomic     **********/
                       sendMessage();   //
                       first_time=false;    //

                   }else{

                       sendMessage();   /************     needs to be atomic     **********/
                       updateThread();  //

                   }
                   editText.setText("");
               }
            }
        });

    }

    public void initThread(){

        thread_key = messagesRef.push().getKey();
        ThreadModel thread_sender = new ThreadModel(my_id, msg.getText(), msg.getTime_stamp());
        ThreadModel thread_receiver = new ThreadModel(receiver_id, msg.getText(), msg.getTime_stamp());
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
}
