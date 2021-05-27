package com.proyectocm.clubpadel.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;

import java.util.Objects;

public class EditEmailActivity extends AppCompatActivity {

    // Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_email);
        setTitle("Cambiar correo");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataOldEmail = findViewById(R.id.insertOldEmail);
        final TextView dataPass = findViewById(R.id.insertPass);
        final TextView dataNewEmail = findViewById(R.id.insertModifiedEmail);

        fetchUserEmail(userId, dataOldEmail);

        buttonSendEmail(userId, dataOldEmail, dataPass, dataNewEmail);
    }

    private void fetchUserEmail(String userId, TextView dataOldEmail) {
        // Request stored info from DB
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbOldEmail = Objects.requireNonNull(document.get("email")).toString();
                    dataOldEmail.setText(fbOldEmail);
                } else {
                    Toast.makeText(getApplicationContext(), "No existe el email.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error en la solicitud." + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // https://firebase.google.com/docs/auth/android/manage-users#set_a_users_email_address
    // https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user
    private void modifyUserEmail(String userId, TextView dataOldEmail, TextView dataPass, TextView dataNewEmail) {
        reauthenticate(dataOldEmail.getText().toString(), dataPass.getText().toString());

        FirebaseUser user = mAuth.getCurrentUser();

        Objects.requireNonNull(user).updateEmail(dataNewEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference modifiedData = db.collection("Users").document(userId);
                        modifiedData
                                .update("email", dataNewEmail.getText().toString())
                                .addOnSuccessListener(aVoid -> {
                                    //Toast.makeText(getApplicationContext(), "Se ha actualizado el campo email.", Toast.LENGTH_LONG).show();
                                })
                                .addOnFailureListener(e -> {
                                    //Toast.makeText(getApplicationContext(), "Error al actualizar el campo email.", Toast.LENGTH_LONG).show();
                                });
                        Toast.makeText(getApplicationContext(), "La dirección de correo se ha actualizado correctamente, se le ha enviado un correo para verificar la nueva dirección.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Contraseña incorrecta, no se ha podido actualizar la dirección de correo.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void buttonSendEmail(String userId, TextView dataOldEmail, TextView dataPass, TextView dataNewEmail) {
        final Button bEditEmail = findViewById(R.id.buttonRequestEditEmail);
        bEditEmail.setOnClickListener(v -> {
            modifyUserEmail(userId, dataOldEmail, dataPass, dataNewEmail);
            Intent jumpTo = new Intent(EditEmailActivity.this, MainActivity.class);
            startActivity(jumpTo);
            finish();
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