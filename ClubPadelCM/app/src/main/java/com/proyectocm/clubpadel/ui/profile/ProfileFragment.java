package com.proyectocm.clubpadel.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;
import com.proyectocm.clubpadel.ui.home.HomeViewModel;
import com.proyectocm.clubpadel.ui.logged.LoggedFragment;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private EditText mEditTextEmail;
    private EditText mEditTextPass;
    private Button bSignUp;
    private Button bSignIn;
    private String email = "";
    private String pass = "";
    FirebaseAuth mAuth;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mAuth = FirebaseAuth.getInstance();

        mEditTextEmail = (EditText) root.findViewById(R.id.editTextTextEmailAddress);
        mEditTextPass = (EditText) root.findViewById(R.id.editTextTextPassword);

        bSignUp = (Button) root.findViewById(R.id.createAccount);
        bSignIn = (Button) root.findViewById(R.id.logIn);

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();


                if(!email.isEmpty() && !pass.isEmpty()){
                    if(pass.length() >= 6){
                        registerUser();
                    }else{
                        textView.setText("Error contraseña demasiado corta, mínimo longitud 6");
                    }
                }else{

                    textView.setText("Error parámetros vacíos");
                }
            }
        });

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();
                loginUser();

            }
        });
        return root;
    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("ResourceType")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    LoggedFragment loggedFragment = new LoggedFragment();
                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.layout.fragment_logged, loggedFragment);
                    fragmentTransaction.commit();
                }else{

                }
            }
        });
    }

    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        });
    }


}

