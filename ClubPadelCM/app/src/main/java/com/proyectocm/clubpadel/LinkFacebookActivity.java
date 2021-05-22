package com.proyectocm.clubpadel;

import android.os.Bundle;
import android.widget.Button;
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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.Objects;

public class LinkFacebookActivity extends AppCompatActivity {

    // Firebase Authentication
    FirebaseAuth mAuth;

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

        Button bFacebook = findViewById(R.id.buttonFacebook2);
        bFacebook.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(LinkFacebookActivity.this, Arrays.asList("email", "public_profile")));

        // Initialize Facebook Login button
        // FaceBook SignIn
        CallbackManager callbackManager = CallbackManager.Factory.create();
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
        Objects.requireNonNull(mAuth.getCurrentUser()).linkWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LinkFacebookActivity.this, "linkWithCredential:success", Toast.LENGTH_LONG).show();
                        //updateUI(user);
                    } else {
                        Toast.makeText(LinkFacebookActivity.this, "Authentication failed.", Toast.LENGTH_LONG).show();
                        //updateUI(null);
                    }
                });
    }

}