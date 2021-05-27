package com.proyectocm.clubpadel.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.proyectocm.clubpadel.R;

public class ForgottenPasswordActivity extends AppCompatActivity {

    // Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
        setTitle("Recuperar contraseña");

        final TextView dataEmail = findViewById(R.id.insertEmailNewPass);

        buttonChangePassword(dataEmail);
    }

    private void buttonChangePassword(TextView dataEmail) {
        final Button bChangePassword = findViewById(R.id.buttonRequestNewPass);
        bChangePassword.setOnClickListener(v -> {
            requestNewPassword(dataEmail);
            Intent jumpTo = new Intent(ForgottenPasswordActivity.this, LoginActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }

    private void requestNewPassword(TextView dataEmail) {
        mAuth.sendPasswordResetEmail(dataEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha enviado un correo para que pueda restablecer la contraseña.", Toast.LENGTH_LONG).show();
                    }
                });

    }

}