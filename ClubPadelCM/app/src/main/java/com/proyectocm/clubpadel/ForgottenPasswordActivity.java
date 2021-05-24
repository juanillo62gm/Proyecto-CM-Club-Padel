package com.proyectocm.clubpadel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ForgottenPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);

        setTitle("Recuperar contraseña");
    }
}