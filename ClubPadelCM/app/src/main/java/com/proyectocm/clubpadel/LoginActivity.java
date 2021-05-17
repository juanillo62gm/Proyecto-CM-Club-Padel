package com.proyectocm.clubpadel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText mEditTextEmail;
    private EditText mEditTextPass;
    private Button bSignUp;
    private Button bSignIn;
    private String email = "";
    private String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextEmail = findViewById(R.id.editTextTextEmailAddress);
        mEditTextPass = findViewById(R.id.editTextTextPassword);

        bSignUp = findViewById(R.id.createAccount);
        bSignIn = findViewById(R.id.logIn);

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();


                if (!email.isEmpty() && !pass.isEmpty()) {
                    if (pass.length() >= 6) {
                        registerUser();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error contraseña demasiado corta, mínimo longitud 6", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Error parámetros vacíos", Toast.LENGTH_LONG).show();
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
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("ResourceType")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                } else {

                }
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                } else {

                }
            }
        });
    }

}