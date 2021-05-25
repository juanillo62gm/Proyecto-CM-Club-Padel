package com.proyectocm.clubpadel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class EditPasswordActivity extends AppCompatActivity {

    // Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        setTitle("Cambiar contraseña");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataEmail = findViewById(R.id.insertRememberEmail);
        final TextView dataNewPass = findViewById(R.id.insertNewPass);
        final TextView dataOldPass = findViewById(R.id.insertOldPass);

        fetchUserEmail(userId, dataEmail);

        buttonChangePassword(dataEmail, dataOldPass, dataNewPass);
    }

    private void buttonChangePassword(TextView dataEmail, TextView dataOldPass, TextView dataNewPass) {
        final Button bChangePassword = findViewById(R.id.buttonRequestEditPass);
        bChangePassword.setOnClickListener(v -> {
            changePassword(dataEmail, dataOldPass, dataNewPass);
            Intent jumpTo = new Intent(EditPasswordActivity.this, MainActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }

    private void fetchUserEmail(String userId, TextView dataEmail) {
        // Request stored info from DB
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbEmail = Objects.requireNonNull(document.get("email")).toString();
                    dataEmail.setText(fbEmail);
                } else {
                    //Toast.makeText(getApplicationContext(), "No existe el email.", Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(getApplicationContext(), "Error en la solicitud." + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void changePassword(TextView dataEmail, TextView dataOldPass, TextView dataNewPass){
        reauthenticate(dataEmail.getText().toString(), dataOldPass.getText().toString());

        FirebaseUser user = mAuth.getCurrentUser();

        user.updatePassword(dataNewPass.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Se ha actualizado la contraseña.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void reauthenticate(String email, String pass) {
        FirebaseUser user = mAuth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);

        Objects.requireNonNull(user).reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    //Toast.makeText(getApplicationContext(), "Re-Autenticado el usuario", Toast.LENGTH_LONG).show();
                });
    }

}