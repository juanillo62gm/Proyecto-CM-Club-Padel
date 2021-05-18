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
import com.google.firebase.auth.FirebaseUser;

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

        bSignUp = findViewById(R.id.signUp);
        bSignIn = findViewById(R.id.signIn);

        checkUser();

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    if (pass.length() >= 6) {
                        registerUser();
                    } else {
                        Toast.makeText(getApplicationContext(), "La contraseña es demasiado corta, la longitud mínima es de 6 caracteres", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos para crear una cuenta", Toast.LENGTH_LONG).show();
                }
            }
        });

        bSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mEditTextEmail.getText().toString();
                pass = mEditTextPass.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    loginUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos para acceder", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(jumpTo);
            Toast.makeText(getApplicationContext(), "Se ha iniciado sesión automáticamente", Toast.LENGTH_LONG).show();
        }
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("ResourceType")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(jumpTo);
                    Toast.makeText(getApplicationContext(), "Se ha iniciado sesión correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha podido acceder", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loginUser();
                    Toast.makeText(getApplicationContext(), "Su cuenta se ha creado correctamente", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Esta cuenta ya existe", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}