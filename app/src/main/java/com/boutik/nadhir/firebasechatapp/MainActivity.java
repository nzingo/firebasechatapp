package com.boutik.nadhir.firebasechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.boutik.nadhir.firebasechatapp.adapters.PagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference presenceRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

//        if(currentUser != null){
//            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        }
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
//        if(currentUser != null){
//            presenceRef = mDatabaseRef.child("users").child(currentUser.getUid()).child("online");
//            presenceRef.setValue(true);
//            presenceRef.onDisconnect().setValue(false);
//        }
        //presenceRef = mDatabaseRef.child("users").child(currentUser.getUid()).child("online");
        //presenceRef.setValue(true);



        ViewPager pager = findViewById(R.id.pager);
        PagerAdapter pager_adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pager_adapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


//        long start = System.nanoTime();
//        long end = System.nanoTime();
//        long time=end-start;
//        Log.i("load_time", String.valueOf(time));



    }


    @Override
    public void onStart() {
        super.onStart();

        if(currentUser == null){
            //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }else{

            /**************************************  presence system  **************************************/

//            presenceRef = mDatabaseRef.child("users").child(currentUser.getUid());
//            final DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
//            connectedRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot snapshot) {
//                    boolean connected = snapshot.getValue(Boolean.class);
//
//                    if (connected) {
//                        presenceRef.child("online").onDisconnect().setValue(false);
//                        presenceRef.child("Last_seen").onDisconnect().setValue(ServerValue.TIMESTAMP);
//                        presenceRef.child("online").setValue(true);
//                    }
//
//                }
//                @Override
//                public void onCancelled(DatabaseError error) {
//                    System.err.println("Listener was cancelled at .info/connected");
//                }
//            });

            /*************************************************************************************************/

            getSupportActionBar().setTitle(currentUser.getEmail());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //presenceRef.onDisconnect().cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            //presenceRef.child("online").setValue(false);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
