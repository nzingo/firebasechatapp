package com.boutik.nadhir.firebasechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ViewPager pager;
    private PagerAdapter pager_adapter;
    private TabLayout tabLayout;
    private DatabaseReference presenceRef;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if(currentUser != null){
            presenceRef = mDatabaseRef.child("users").child(currentUser.getUid()).child("online");
            presenceRef.setValue(true);
            presenceRef.onDisconnect().setValue(false);
        }



        pager = findViewById(R.id.pager);
        pager_adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pager_adapter);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

    }


    @Override
    public void onStart() {
        super.onStart();

        if(currentUser == null){
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
        }else{

            getSupportActionBar().setTitle(currentUser.getEmail());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
       // presenceRef.onDisconnect().cancel();
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
            presenceRef.setValue(false);
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
