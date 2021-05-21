package com.proyectocm.clubpadel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LinkFacebookActivity extends AppCompatActivity {

    // Firebase Authentication
    FirebaseAuth mAuth;

    // FaceBook SignIn
    private CallbackManager callbackManager;

    private Button bFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_facebook);
        setTitle("Vincular FaceBook");

        // Debug Mode Facebook Login Tokens
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }

        bFacebook = findViewById(R.id.buttonFacebook2);

        bFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LinkFacebookActivity.this, Arrays.asList("email", "public_profile"));
            }
        });


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

    }

    // Start Facebook SignIn
    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LinkFacebookActivity.this, "linkWithCredential:success", Toast.LENGTH_LONG).show();
                            FirebaseUser user = task.getResult().getUser();
                            //updateUI(user);
                        } else {
                            Toast.makeText(LinkFacebookActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                            //updateUI(null);
                        }
                    }
                });
    }

}