package com.proyectocm.clubpadel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ModifyUserAccountActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 111;
    // Firebase Firestore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    // Google SignIn
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_user_account);
        setTitle("Editar perfil");

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataName = findViewById(R.id.insertModifiedName);
        final TextView dataSurname = findViewById(R.id.insertModifiedSurname);
        final TextView dataPhone = findViewById(R.id.insertModifiedPhone);

        fetchUserData(userId, dataName, dataSurname, dataPhone);

        buttonSendUserData(userId, dataName, dataSurname, dataPhone);

        SignInGoogle();

        buttonEditEmail();
        buttonEditPass();
        buttonRemoveAccount();
        buttonLinkFacebook();
        buttonLinkGoogle();

    }

    private void fetchUserData(String userId, TextView dataName, TextView dataSurname, TextView dataPhone) {

        // Request stored info from DB
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

    private void buttonEditEmail() {
        final Button bEditEmail = findViewById(R.id.buttonEditEmail);
        bEditEmail.setOnClickListener(v -> {
            Intent jumpTo = new Intent(ModifyUserAccountActivity.this, EditEmailActivity.class);
            startActivity(jumpTo);
        });
    }

    private void buttonEditPass() {
        final Button bEditEmail = findViewById(R.id.buttonEditPass);
        bEditEmail.setOnClickListener(v -> {
            Intent jumpTo = new Intent(ModifyUserAccountActivity.this, EditPasswordActivity.class);
            startActivity(jumpTo);
        });
    }

    private void buttonRemoveAccount() {
        final Button bRemoveAccount = findViewById(R.id.buttonRemoveAccount);
        bRemoveAccount.setOnClickListener(v -> {
            Intent jumpTo = new Intent(ModifyUserAccountActivity.this, RemoveAccountActivity.class);
            startActivity(jumpTo);
            finish();
        });
    }

    private void buttonLinkFacebook() {
        final Button buttonFacebook = findViewById(R.id.buttonLinkFB);

        AccessToken token = AccessToken.getCurrentAccessToken();
        // [START auth_fb_cred]
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        // [END auth_fb_cred]

        buttonFacebook.setOnClickListener(v -> {
            mAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Funciona.", Toast.LENGTH_LONG).show();
                                //Log.d(TAG, "linkWithCredential:success");
                                //FirebaseUser user = task.getResult().getUser();
                                //updateUI(user);
                            } else {
                                Toast.makeText(getApplicationContext(), "No Funciona.", Toast.LENGTH_LONG).show();
                                //Log.w(TAG, "linkWithCredential:failure", task.getException());
                                //Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        });
    }

    private void buttonLinkGoogle() {
        final Button buttonGoogle = findViewById(R.id.buttonLinkGoogle);

        buttonGoogle.setOnClickListener(v -> {
            signIn();
        });
    }

    private void LinkGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        // [END auth_google_cred]

        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Funciona.", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "linkWithCredential:success");
                            //FirebaseUser user = task.getResult().getUser();
                            //updateUI(user);
                        } else {
                            Toast.makeText(getApplicationContext(), "No Funciona.", Toast.LENGTH_LONG).show();
                            //Log.w(TAG, "linkWithCredential:failure", task.getException());
                            //Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
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