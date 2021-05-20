package com.proyectocm.clubpadel.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.proyectocm.clubpadel.LoginActivity;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;

import java.util.concurrent.Executor;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Texto central
        /*
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        */

        // Información del Usuario
        final TextView dataUserEmail = root.findViewById(R.id.userEmail);
        final TextView dataUserName = root.findViewById(R.id.userName);
        final TextView dataUserSignIn = root.findViewById(R.id.userSignIn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // Id of the provider (ex: google.com)
                dataUserSignIn.setText(profile.getProviderId());

                // UID specific to the provider
                //String uid = profile.getUid();

                // Name, email address, and profile photo Url
                dataUserName.setText(profile.getDisplayName());
                dataUserEmail.setText(profile.getEmail());
            }
        }

        // SignOut
        final Button buttonLogOut = root.findViewById(R.id.signOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    FirebaseAuth.getInstance().signOut(); //signout firebase
                    Intent jumpTo = new Intent(getActivity(), LoginActivity.class);
                    startActivity(jumpTo);
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Se ha cerrado sesión.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Se ha producido un error al cerrar sesión.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}

