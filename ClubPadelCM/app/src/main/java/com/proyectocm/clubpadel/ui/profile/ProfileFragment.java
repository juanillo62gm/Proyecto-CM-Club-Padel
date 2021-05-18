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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proyectocm.clubpadel.LoginActivity;
import com.proyectocm.clubpadel.MainActivity;
import com.proyectocm.clubpadel.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        // Texto central
        final TextView textView = root.findViewById(R.id.text_notifications);
        profileViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        // Información del Usuario
        final TextView textMail = root.findViewById(R.id.userEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        textMail.setText(user.getEmail());

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
                    Toast.makeText(getActivity(), "Se ha cerrado sesión", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Error al cerrar sesión", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}

