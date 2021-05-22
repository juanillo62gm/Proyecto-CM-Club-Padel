package com.proyectocm.clubpadel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class CreateAccountActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // Firebase RealtimeDatabase
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    // Email SignUp
    private EditText dataName, dataSurname, dataPhone, dataEmail, dataPassword;
    private String email = "", pass = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setTitle("Crear cuenta");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://club-padel-cm-default-rtdb.europe-west1.firebasedatabase.app/");

        dataName = findViewById(R.id.insertSignUpName);
        dataSurname = findViewById(R.id.insertSignUpSurname);
        dataPhone = findViewById(R.id.insertSignUpPhone);
        dataEmail = findViewById(R.id.insertSignUpEmail);
        dataPassword = findViewById(R.id.insertSignUpPass);

        Button bSignUp = findViewById(R.id.buttonNewAccount);

        bSignUp.setOnClickListener(v -> {
            email = dataEmail.getText().toString();
            pass = dataPassword.getText().toString();

            if (!email.isEmpty() && !pass.isEmpty()) {
                if (pass.length() >= 6) {
                    registerUser();
                } else {
                    Toast.makeText(getApplicationContext(), "La contraseña es demasiado corta, la longitud mínima es de 6 carácteres.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Complete todos los campos para crear una cuenta.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loginUser();

                // Start Database Store User Data
                mReference = mDatabase.getReference("Users");

                String name = dataName.getText().toString();
                String surname = dataSurname.getText().toString();
                String phone = dataPhone.getText().toString();
                String email = dataEmail.getText().toString();

                User user = new User(name, surname, phone, email);
                String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                mReference.child(userId).setValue(user);
                // End Database Store User Data

                Toast.makeText(getApplicationContext(), "Su cuenta ha sido creada.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cuenta ya existente, inicie sesión con Google o Facebook.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent jumpTo = new Intent(CreateAccountActivity.this, MainActivity.class);
                startActivity(jumpTo);
                Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con email.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido acceder con email.", Toast.LENGTH_LONG).show();
            }
        });
    }

}