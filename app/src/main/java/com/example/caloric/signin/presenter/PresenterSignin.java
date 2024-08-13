package com.example.caloric.signin.presenter;

import android.content.Context;

import com.example.caloric.respiratories.RepositoryLocal;
import com.example.caloric.respiratories.RepositoryRemote;

public class PresenterSignin {
    private static final String TAG = "PresenterFirebaseFirea";

    private SigninInterface signinInterface;
    private RepositoryRemote repositoryRemote;
    private Context context;
    private RepositoryLocal repositoryLocal;


    public PresenterSignin(SigninInterface interfaceRegister) {
        this.signinInterface = interfaceRegister;
    }

    public PresenterSignin(Context context) {

        this.context = context;
        repositoryLocal = new RepositoryLocal(context);

    }

    public void createUserWithEmailAndPassword(String email, String password) {
        repositoryRemote = new RepositoryRemote(signinInterface);
        repositoryRemote.createUserWithEmailAndPassword(email, password);
    }



}
