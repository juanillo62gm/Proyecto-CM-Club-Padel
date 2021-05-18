package com.proyectocm.clubpadel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText mEditTextEmail;
    private EditText mEditTextPass;
    private Button bFacebook;
    private Button bGoogle;
    private Button bSignUp;
    private Button bSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private AccessToken accessToken;
    private final static int RC_SIGN_IN = 111;
    private String email = "";
    private String pass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        accessToken = AccessToken.getCurrentAccessToken();

        mEditTextEmail = findViewById(R.id.editTextTextEmailAddress);
        mEditTextPass = findViewById(R.id.editTextTextPassword);

        bFacebook = findViewById(R.id.buttonFacebook);
        bGoogle = findViewById(R.id.buttonGoogle);
        bSignUp = findViewById(R.id.buttonSignUp);
        bSignIn = findViewById(R.id.buttonSignIn);

        SignInGoogle();

        /* En proceso Facebook
        bFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleFacebookAccessToken(accessToken);
                Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con Google correctamente", Toast.LENGTH_LONG).show();
            }
        });
        */

        bGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con Google correctamente", Toast.LENGTH_LONG).show();
                }
        });

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

    protected void onStart() {
        super.onStart();

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
                    Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con email correctamente", Toast.LENGTH_LONG).show();
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

    private void SignInGoogle(){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(getApplicationContext(), "firebaseAuthWithGoogle:" + account.getId(), Toast.LENGTH_LONG).show();
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "Google sign in failed" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(jumpTo);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure" + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /* En proceso Facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Toast.makeText(getApplicationContext(), "handleFacebookAccessToken:" + token, Toast.LENGTH_LONG).show();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplicationContext(), "signInWithCredential:success" + token, Toast.LENGTH_LONG).show();

                            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(jumpTo);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "signInWithCredential:failure" + token, Toast.LENGTH_LONG).show();
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
     */

}