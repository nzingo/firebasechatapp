package com.boutik.nadhir.firebasechatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button submit_login;
    private TextInputLayout email;
    private TextInputLayout password;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit_login = findViewById(R.id.submit_login);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        submit_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_user(email.getEditText().getText().toString()
                        ,password.getEditText().getText().toString());
            }
        });

    }


    private void login_user(String email,String password){

        spinner.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(LogInActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(LogInActivity.this,"sign up failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }
}
