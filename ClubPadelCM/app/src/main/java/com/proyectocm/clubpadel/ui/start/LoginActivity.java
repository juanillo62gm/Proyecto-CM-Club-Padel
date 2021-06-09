package com.proyectocm.clubpadel.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.proyectocm.clubpadel.BuildConfig;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;

import java.util.Collections;

public class LoginActivity extends AppCompatActivity {

    private final static int RC_SIGN_IN = 111;
    // Firebase Authentication
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    // Email SignIn
    private EditText dataEmail, dataPassword;
    // Google SignIn
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        dataEmail = findViewById(R.id.insertSignInEmail);
        dataPassword = findViewById(R.id.insertSignInPass);

        requestGoogleToken();
        debugModeFacebook();
        requestFacebookToken();

        buttonSignInWithFacebook();
        buttonSignInWithGoogle();
        buttonSignInWithEmail();

        buttonSignUp();
        buttonForgottenPass();

    }

    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(jumpTo);
            Toast.makeText(getApplicationContext(), "Se ha iniciado sesión automáticamente.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void buttonSignInWithFacebook() {
        Button bFacebook = findViewById(R.id.buttonSignInFacebook);
        bFacebook.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Collections.singleton("email")));
    }

    private void buttonSignInWithGoogle() {
        Button bGoogle = findViewById(R.id.buttonSignInGoogle);
        bGoogle.setOnClickListener(v -> {
            signInWithGoogle();
            Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con Google.", Toast.LENGTH_LONG).show();
        });
    }

    private void buttonSignInWithEmail() {
        Button bEmail = findViewById(R.id.buttonSignInEmail);
        bEmail.setOnClickListener(v -> {
            String email = dataEmail.getText().toString();
            String pass = dataPassword.getText().toString();

            if (!email.isEmpty() && !pass.isEmpty()) {
                signInWithEmail(email, pass);
            } else {
                Toast.makeText(getApplicationContext(), "Complete todos los campos para acceder.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void buttonSignUp() {
        Button bSignUp = findViewById(R.id.buttonSignUp);
        bSignUp.setOnClickListener(v -> {
            Intent jumpTo = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(jumpTo);
        });
    }

    private void buttonForgottenPass() {
        Button bForgottenPass = findViewById(R.id.buttonForgottenPass);
        bForgottenPass.setOnClickListener(v -> {
            Intent jumpTo = new Intent(LoginActivity.this, ForgottenPasswordActivity.class);
            startActivity(jumpTo);
        });
    }

    private void signInWithEmail(String email, String pass) {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(jumpTo);
                Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con email.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "No se ha podido acceder con email.", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Facebook Login
    private void debugModeFacebook() {
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
    }

    private void requestFacebookToken() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Se ha cancelado el inicio de sesión con Facebook.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Se ha producido un error al iniciar sesión con Facebook" + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(LoginActivity.this, "Se ha iniciado sesión con Facebook.", Toast.LENGTH_LONG).show();
                        Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(jumpTo);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Cuenta ya existente, inicie sesión con Email o Google.", Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Google Login
    private void requestGoogleToken() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @SuppressWarnings("deprecation")
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(jumpTo);
                        finish();
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión con Google." + task.getException(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Start Facebook Login
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // End Facebook Login
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión con Google." + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}