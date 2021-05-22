package com.proyectocm.clubpadel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ModifyUserAccountActivity extends AppCompatActivity {

    // Firebase Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_account);
        setTitle("Editar perfil");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataName = findViewById(R.id.insertModifiedName);
        final TextView dataSurname = findViewById(R.id.insertModifiedSurname);
        final TextView dataPhone = findViewById(R.id.insertModifiedPhone);
        final TextView dataEmail = findViewById(R.id.insertModifiedEmail);

        fetchUserData(userId, dataName, dataSurname, dataPhone, dataEmail);

        buttonSendUserData(userId, dataName, dataSurname, dataPhone);

        buttonSendEmail(dataEmail);

    }

    private void fetchUserData(String userId, TextView dataName, TextView dataSurname, TextView dataPhone, TextView dataEmail) {

        // Request stored info from DB
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbName = Objects.requireNonNull(document.get("name")).toString();
                    String fbSurname = Objects.requireNonNull(document.get("surname")).toString();
                    String fbPhone = Objects.requireNonNull(document.get("phone")).toString();
                    String fbEmail = Objects.requireNonNull(document.get("email")).toString();
                    dataName.setText(fbName);
                    dataSurname.setText(fbSurname);
                    dataPhone.setText(fbPhone);
                    dataEmail.setText(fbEmail);

                } else {
                    Toast.makeText(getApplicationContext(), "No existe el usuario.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error en la solicitud." + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void modifyUserData(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {

        DocumentReference modifiedData = db.collection("Users").document(userId);
        modifiedData
                .update("name", dataName.getText().toString())
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(getApplicationContext(), "Se ha actualizado el campo nombre.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getApplicationContext(), "Error al actualizar el campo nombre.", Toast.LENGTH_LONG).show();
                });
        modifiedData
                .update("surname", dataSurname.getText().toString())
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(getApplicationContext(), "Se ha actualizado el campo apellidos.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getApplicationContext(), "Error al actualizar el campo apellidos.", Toast.LENGTH_LONG).show();
                });
        modifiedData
                .update("phone", dataPhone.getText().toString())
                .addOnSuccessListener(aVoid -> {
                    //Toast.makeText(getApplicationContext(), "Se ha actualizado el campo móvil.", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    //Toast.makeText(getApplicationContext(), "Error al actualizar el campo móvil.", Toast.LENGTH_LONG).show();
                });
    }

    private void buttonSendUserData(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {
        final Button bEditProfile = findViewById(R.id.buttonStoreNewProfileInfo);
        bEditProfile.setOnClickListener(v -> {
            modifyUserData(userId, dataName, dataSurname, dataPhone);
            Toast.makeText(getApplicationContext(), "Se han modificado los datos del perfil.", Toast.LENGTH_LONG).show();
            Intent jumpTo = new Intent(ModifyUserAccountActivity.this, MainActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }

    // Hay que iniciar sesión justo antes de modificar este tipo de cosas (WIP)
    // https://firebase.google.com/docs/auth/android/manage-users#set_a_users_email_address
    // https://firebase.google.com/docs/auth/android/manage-users#re-authenticate_a_user
    private void modifyUserEmail(TextView dataEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updateEmail(dataEmail.getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha actualizado el correo correctamente", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Se ha producido un error al actualizar el correo", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void buttonSendEmail(TextView dataEmail) {
        final Button bEditEmail = findViewById(R.id.buttonRequestEditEmail);
        bEditEmail.setOnClickListener(v -> {
            modifyUserEmail(dataEmail);
            Toast.makeText(getApplicationContext(), "Se ha enviado un email para verificar la nueva dirección de email.", Toast.LENGTH_LONG).show();
            Intent jumpTo = new Intent(ModifyUserAccountActivity.this, MainActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }
}