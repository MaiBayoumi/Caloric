package com.example.caloric.login;

import android.content.Context;

import com.airbnb.lottie.L;

public class PresenterLogin {
    public LoginIterface loginIterface;
    private Context context;
    private static final String TAG = "PresenterFirebaseFireau";
    private RepositoryRemote repositoryRemote;
    private RepositoryLocal repositoryLocal;

    public PresenterLogin(Context context, LoginIterface loginIterface) {
        this.context = context;
        this.loginIterface = loginIterface;
    }

    public PresenterLogin(Context context) {
        this.context = context;
        repositoryLocal = new RepositoryLocal(context);
    }


    public void Login(String email, String password) {

        repositoryRemote = new RepositoryRemote(loginIterface, context);
        repositoryRemote.signIn(email, password);


    }

    public void signInGoogle() {

        repositoryRemote = new RepositoryRemote(loginIterface, context);
        repositoryRemote.signInGoogle();

    }
}

