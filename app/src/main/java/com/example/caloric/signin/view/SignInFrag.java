package com.example.caloric.signin.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.caloric.MainActivity;
import com.example.caloric.R;
import com.example.caloric.network.NetworkChecker;
import com.example.caloric.signin.presenter.PresenterSignin;
import com.example.caloric.signin.presenter.SigninInterface;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;

public class SignInFrag extends Fragment implements SigninInterface {
    private TextInputEditText signUp_email, signUp_password, confirmPassword;
    private Button register;
    private View view;
    private NetworkChecker networkChecker;
    private PresenterSignin presenterSignin;
    private String email;
    private static final String TAG = "Register";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        signUp_email = view.findViewById(R.id.emailS);
        signUp_password = view.findViewById(R.id.passwordS);
        register = view.findViewById(R.id.signinbtn);
        confirmPassword = view.findViewById(R.id.confirmpassword);
        networkChecker = NetworkChecker.getInstance();
        presenterSignin = new PresenterSignin(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = signUp_email.getText().toString();
                String password = signUp_password.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if (!networkChecker.checkIfInternetIsConnected()) {
                    Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to register.", Toast.LENGTH_SHORT).show();

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    Toast.makeText(getContext(), "Registring", Toast.LENGTH_SHORT).show();

                    if ((!email.isEmpty()) && (!password.isEmpty()) && (password.equals(confirm))) {
                        Toast.makeText(getContext(), "Enter your data", Toast.LENGTH_SHORT).show();
                        presenterSignin.createUserWithEmailAndPassword(email, password);


                    } else {
                        if (email.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                        } else if (password.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(confirm)) {
                            Toast.makeText(getContext(), "Password not identical", Toast.LENGTH_SHORT).show();

                        }

                    }

                }





            }


        });
    }

    @Override
    public void NormalSignIn(Task<AuthResult> task) {
        if (task.isSuccessful()) {


            Toast.makeText(requireContext(), "Registration was successful", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(view).navigate(R.id.action_signInFrag_to_recommendationFrag);


        } else {
            Exception exception = task.getException();
            if (exception == null) {
                Toast.makeText(getContext(), "UnExpected error occurred", Toast.LENGTH_SHORT).show();
            } else if (exception.getClass().equals(FirebaseNetworkException.class)) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();

            } else {
                if (((FirebaseAuthException) exception).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {

                    Toast.makeText(getContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }


        }
    }
    }
