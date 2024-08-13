package com.example.caloric.signin.presenter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface SigninInterface {
    void NormalSignIn(Task<AuthResult> task);
}
