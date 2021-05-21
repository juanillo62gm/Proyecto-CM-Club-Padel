package com.proyectocm.clubpadel;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
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

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    // Firebase Authentication
    private FirebaseAuth mAuth;

    // FaceBook SignIn
    private Button bFacebook;
    private CallbackManager callbackManager;

    // Google SignIn
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 111;
    private Button bGoogle;

    // Email SignIn
    private EditText dataEmail, dataPassword;
    private String email = "", pass = "";
    private Button bSignUp, bEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        dataEmail = findViewById(R.id.insertSignInEmail);
        dataPassword = findViewById(R.id.insertSignInPass);

        bGoogle = findViewById(R.id.buttonGoogle);
        bFacebook = findViewById(R.id.buttonFacebook);
        bSignUp = findViewById(R.id.buttonSignUp);
        bEmail = findViewById(R.id.buttonSignIn);

        SignInGoogle();

        // Debug Mode Facebook Login Tokens
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
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

        bFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
            }
        });

        bGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con Google.", Toast.LENGTH_LONG).show();
                }
        });

        bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = dataEmail.getText().toString();
                pass = dataPassword.getText().toString();

                if (!email.isEmpty() && !pass.isEmpty()) {
                    loginUser();
                } else {
                    Toast.makeText(getApplicationContext(), "Complete todos los campos para acceder.", Toast.LENGTH_LONG).show();
                }
            }
        });

        bSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpTo = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(jumpTo);
            }
        });

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

    // Start Email SignIn
    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @SuppressLint("ResourceType")
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(jumpTo);
                    Toast.makeText(getApplicationContext(), "Se ha iniciado sesión con email.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No se ha podido acceder con email.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // End Email SignIn

    // Start Google SignIn
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
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión con Google." + e.getMessage(), Toast.LENGTH_LONG).show();
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
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(jumpTo);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "No se ha podido iniciar sesión con Google." + task.getException(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    // End Google SignIn

    // Start Facebook SignIn
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(LoginActivity.this, "Se ha iniciado sesión con Facebook.", Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent jumpTo = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(jumpTo);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Cuenta ya existente, inicie sesión con Email o Google.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    // End Facebook SignIn

}