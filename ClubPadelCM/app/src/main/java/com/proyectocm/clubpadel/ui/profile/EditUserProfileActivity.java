package com.proyectocm.clubpadel.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;

import java.util.Objects;

public class EditUserProfileActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 111;
    // Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // Google SignIn
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        setTitle("Editar perfil");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataName = findViewById(R.id.insertModifiedName);
        final TextView dataSurname = findViewById(R.id.insertModifiedSurname);
        final TextView dataPhone = findViewById(R.id.insertModifiedPhone);

        SignInGoogle();

        fetchUserDataFromDB(userId, dataName, dataSurname, dataPhone);

        buttonEditProfile(userId, dataName, dataSurname, dataPhone);

        buttonEditEmail();
        buttonEditPass();

        //buttonLinkFacebook();
        buttonLinkGoogle();

        buttonRemoveAccount();

    }

    private void fetchUserDataFromDB(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbName = Objects.requireNonNull(document.get("name")).toString();
                    String fbSurname = Objects.requireNonNull(document.get("surname")).toString();
                    String fbPhone = Objects.requireNonNull(document.get("phone")).toString();
                    dataName.setText(fbName);
                    dataSurname.setText(fbSurname);
                    dataPhone.setText(fbPhone);
                } else {
                    Toast.makeText(getApplicationContext(), "No existe el usuario.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Error en la solicitud." + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void buttonEditProfile(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {
        final Button bEditProfile = findViewById(R.id.buttonStoreNewProfileInfo);
        bEditProfile.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditUserProfileActivity.this)
                    .setTitle("Se van a modificar su perfil").setMessage("¿Está seguro?")
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        modifyUserData(userId, dataName, dataSurname, dataPhone);
                        Toast.makeText(getApplicationContext(), "Se han modificado los datos del perfil.", Toast.LENGTH_LONG).show();
                        Intent jumpTo = new Intent(EditUserProfileActivity.this, MainActivity.class);
                        startActivity(jumpTo);
                        finish();
                    })
                    .setNegativeButton(R.string.decline, (dialog, which) -> {

                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void modifyUserData(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {
        DocumentReference modifiedData = db.collection("Users").document(userId);
        modifiedData
                .update("name", dataName.getText().toString());
        modifiedData
                .update("surname", dataSurname.getText().toString());
        modifiedData
                .update("phone", dataPhone.getText().toString());
    }

    private void buttonEditEmail() {
        final Button bEditEmail = findViewById(R.id.buttonEditEmail);
        bEditEmail.setOnClickListener(v -> {
            Intent jumpTo = new Intent(EditUserProfileActivity.this, EditEmailActivity.class);
            startActivity(jumpTo);
        });
    }

    private void buttonEditPass() {
        final Button bEditEmail = findViewById(R.id.buttonEditPass);
        bEditEmail.setOnClickListener(v -> {
            Intent jumpTo = new Intent(EditUserProfileActivity.this, EditPasswordActivity.class);
            startActivity(jumpTo);
        });
    }

    private void buttonLinkFacebook() {
        final Button buttonFacebook = findViewById(R.id.buttonLinkFB);
        AccessToken token = AccessToken.getCurrentAccessToken();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        buttonFacebook.setOnClickListener(v -> Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha vinculado correctamente la cuenta de Facebook.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se ha podido vincular la cuenta de Facebook", Toast.LENGTH_LONG).show();
                    }
                }));
    }

    private void buttonLinkGoogle() {
        final Button buttonGoogle = findViewById(R.id.buttonLinkGoogle);
        buttonGoogle.setOnClickListener(v -> signIn());
    }

    private void buttonRemoveAccount() {
        final Button bRemoveAccount = findViewById(R.id.buttonRemoveAccount);
        bRemoveAccount.setOnClickListener(v -> {
            Intent jumpTo = new Intent(EditUserProfileActivity.this, RemoveAccountActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }

    private void LinkGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Se ha vinculado correctamente la cuenta de Google.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No se ha podido vincular la cuenta de Google", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void SignInGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    LinkGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión con Google." + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}