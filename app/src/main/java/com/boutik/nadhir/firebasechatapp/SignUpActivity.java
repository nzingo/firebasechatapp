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

import com.boutik.nadhir.firebasechatapp.models.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button submit_signup;
    private TextInputLayout user_name;
    private TextInputLayout email;
    private TextInputLayout password;
    private ProgressBar spinner;
    private DatabaseReference root_ref;
    private DatabaseReference user_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        root_ref = FirebaseDatabase.getInstance().getReference();


        user_name = findViewById(R.id.user_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit_signup = findViewById(R.id.submit_signup);
        spinner = findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        submit_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup_user(email.getEditText().getText().toString()
                            ,password.getEditText().getText().toString()
                            ,user_name.getEditText().getText().toString());

            }
        });
    }

    private void signup_user(final String email, String password, final String name){

        final UserModel user = new UserModel(name,email);
        spinner.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser current_user = mAuth.getCurrentUser();
                            user_ref = root_ref.child("users").child(current_user.getUid());
                            user_ref.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){

                                        Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            });


                        }else {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this,"sign up failed",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
