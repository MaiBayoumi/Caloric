package com.example.caloric.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloric.MainActivity;
import com.example.caloric.NetworkChecker;
import com.example.caloric.R;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginFrag extends Fragment implements LoginIterface{

    private TextInputEditText inputemail;
    private TextInputEditText inputpassword;
    private SignInButton googeSignIn;
    private Button Login;
    private ImageView img_eye;
    private TextView register;
    private AppCompatButton asAguestbtn;
    private NetworkChecker networkChecker;
    private PresenterLogin presenterlogin;
    private View view;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    private static final String TAG = "SignIn";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view =view;
        inputemail = view.findViewById(R.id.et_email);
        inputpassword = view.findViewById(R.id.et_password);
        Login = view.findViewById(R.id.loginbtn);
        register = view.findViewById(R.id.register);
        googeSignIn = view.findViewById(R.id.googlebtn);
        asAguestbtn = view.findViewById(R.id.guestbtn);
        NetworkChecker networkChecker = NetworkChecker.getInstance();


        SharedPreferences sharedPref = requireContext().getSharedPreferences(
                "setting", Context.MODE_PRIVATE);
        sharedPref.edit().putBoolean("first_look", true).apply();

        presenterlogin = new PresenterLogin(requireContext(), this);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputemail.getText().toString();
                String password = inputpassword.getText().toString();

                if (!networkChecker.checkIfInternetIsConnected()) {
                    Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to sign in.", Toast.LENGTH_SHORT).show();

                } else if (networkChecker.checkIfInternetIsConnected()) {
                   Toast.makeText(getContext(),"Please wait while signing in",Toast.LENGTH_SHORT).show();


                    if ((!email.isEmpty()) && (!password.isEmpty())) {
                        presenterlogin.Login(email, password);


                    } else {
                        if (email.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                        } else if (password.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Object SignInDirections;
                Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_signInFrag);


            }
        });
    }

    @Override
    public void NormalLogin((Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.i(TAG, "onActivityResult: " + firebaseAuth.getCurrentUser().getEmail());

            Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show();


            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_signInFrag);
        } else {
            Exception exception = task.getException();
            if (exception == null) {
                Toast.makeText(getContext(), "UnExpected error occurred", Toast.LENGTH_SHORT).show();
            } else {
                if (exception.getClass().equals(FirebaseAuthException.class)) {
                    if (((FirebaseAuthException) exception).getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
                        Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (exception.getClass().equals(FirebaseNetworkException.class)) {
                    Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();
                }
                else if (task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted.")) {
                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    @Override
    public void onCompleteGoogleSignIn(Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Toast.makeText(requireContext(), "Sign in with Google was successful", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(view).navigate(R.id.action_loginFrag_to_signInFrag);
        } else {
            Toast.makeText(requireContext(), "Sign in with google failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompleteSignInIntent(Intent signInIntent, int i) {
        startActivityForResult(signInIntent, 1000);
    }
}