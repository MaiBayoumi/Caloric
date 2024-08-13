package com.example.caloric.login;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.caloric.model.MealsItem;
import com.example.caloric.network.RemotDataSource;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.List;

public class RepositoryRemote {
    private LoginIterface loginIterface;
    //private mainIterface interfaceMain;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private RemotDataSource retrofitClient = RemotDataSource.getInstance();
    private List<MealsItem> meals = new ArrayList<>();
    private Context context;

    public RepositoryRemote(LoginIterface loginIterface, Context context) {
        this.loginIterface = loginIterface;
        this.context = context;
    }

    public RepositoryRemote() {

    }

//    public RepositoryRemote(mainInterface interfaceMain) {
//        this.interfaceMain = interfaceMain;
//    }

    public void signIn(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                loginIterface.NormalLogin(task);

            }
        }) ;

    }

    public void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("86693954186-dqg19ueeebjmggpvi0bs13d576p6vjdt.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);


        Intent signInIntent = gsc.getSignInIntent();


        loginIterface.onCompleteSignInIntent(signInIntent, 1000);
    }

    public void respondToActivityResultOfGoogleSignIn(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(task.getResult().getIdToken(), null)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    loginIterface.onCompleteGoogleSignIn(task);
                }
            }) ;
        }
    }
}
