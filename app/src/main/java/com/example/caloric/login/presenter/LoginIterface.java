package com.example.caloric.login.presenter;

import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface LoginIterface {
    void NormalLogin(Task<AuthResult> task);

    void onCompleteGoogleSignIn(Task<AuthResult> task);

    void onCompleteSignInIntent(Intent signInIntent, int i);
}
