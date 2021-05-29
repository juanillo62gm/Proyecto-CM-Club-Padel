package com.proyectocm.clubpadel.ui.profile;

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
import com.proyectocm.clubpadel.R;

import java.util.Objects;

public class RemoveAccountActivity extends AppCompatActivity {

    // Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Firebase Authentication
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_account);
        setTitle("Eliminar cuenta");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataEmail = findViewById(R.id.insertRemoveEmail);
        final TextView dataPass = findViewById(R.id.insertRemovePass);

        fetchUserEmail(userId, dataEmail);

        buttonRemoveAccount(userId, dataEmail, dataPass);
    }

    private void buttonRemoveAccount(String userId, TextView dataEmail, TextView dataPass) {
        final Button bRemoveAccount = findViewById(R.id.buttonRequestRemoveAccount);
        bRemoveAccount.setOnClickListener(v -> {
            removeUser(userId, dataEmail, dataPass);
            finish();
            this.finishAffinity();
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
                }
            }
        });
    }

    private void removeUser(String userId, TextView dataEmail, TextView dataPass) {
        reauthenticate(dataEmail.getText().toString(), dataPass.getText().toString());

        FirebaseUser user = mAuth.getCurrentUser();

        Objects.requireNonNull(user).delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        removeUserFromDB(userId);
                        Toast.makeText(getApplicationContext(), "Se ha eliminado la cuenta.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void reauthenticate(String email, String pass) {
        FirebaseUser user = mAuth.getCurrentUser();

        AuthCredential credential = EmailAuthProvider.getCredential(email, pass);

        Objects.requireNonNull(user).reauthenticate(credential)
                .addOnCompleteListener(task -> {
                });
    }

    private void removeUserFromDB(String userId) {
        db.collection("Users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(getApplicationContext(), "Se ha eliminado al usuario correctamente de la DB.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getApplicationContext(), "No se ha podido eliminar al usuario de la DB.", Toast.LENGTH_LONG).show();

                });

    }

}