package com.example.caloric.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caloric.view.HostedActivity;
import com.example.caloric.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignIn extends AppCompatActivity {
    EditText email;
    EditText password;
    EditText confirmpass;
    EditText name;
    Button signin;
    Intent intent;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        email = findViewById(R.id.emailS);
        password = findViewById(R.id.passwordS);
        confirmpass = findViewById(R.id.confirmpassword);
        name = findViewById(R.id.nameS);
        signin = findViewById(R.id.signinbtn);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        intent = new Intent();
        mAuth = FirebaseAuth.getInstance();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NormalSignIn();
            }
        });
    }

    public void NormalSignIn() {
        if(name.getText().toString().equals("") ||email.getText().toString().equals("") ||password.getText().toString().equals("") ||confirmpass.getText().toString().equals("") ){
            Toast.makeText(this, "all fields are required", Toast.LENGTH_SHORT).show();
        }else if(!password.getText().toString().equals(confirmpass.getText().toString())){
            Toast.makeText(this, "password not Match Confirm Password", Toast.LENGTH_SHORT).show();
        } else {
            signin.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            String emaildb = email.getText().toString();
            String passworddb = password.getText().toString();
            String namedb = name.getText().toString();
            new Thread(() -> {
            mAuth.createUserWithEmailAndPassword(emaildb, passworddb)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                intent.setClass(SignIn.this, HostedActivity.class);
                                mAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(namedb).build());
                                startActivity(intent);
                            } else {
                                signin.setVisibility(View.VISIBLE);
                                Toast.makeText(SignIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
            }).start();
        }
    }
}
