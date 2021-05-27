package com.proyectocm.clubpadel.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectocm.clubpadel.R;
import com.proyectocm.clubpadel.ui.start.LoginActivity;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    // Firebase Firestore
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        final TextView dataName = root.findViewById(R.id.fetchName);
        final TextView dataSurname = root.findViewById(R.id.fetchSurname);
        final TextView dataPhone = root.findViewById(R.id.fetchPhone);
        final TextView dataUserEmail = root.findViewById(R.id.fetchEmail);

        // Request stored info from DB
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbName = Objects.requireNonNull(document.get("name")).toString();
                    String fbSurname = Objects.requireNonNull(document.get("surname")).toString();
                    String fbPhone = Objects.requireNonNull(document.get("phone")).toString();
                    String fbEmail = Objects.requireNonNull(document.get("email")).toString();
                    dataName.setText(fbName);
                    dataSurname.setText(fbSurname);
                    dataPhone.setText(fbPhone);
                    dataUserEmail.setText(fbEmail);
                } else {
                    Toast.makeText(getActivity(), "No existe el usuario", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error en la solicitud" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });

        // Edit Profile Button
        final Button bEditProfile = root.findViewById(R.id.buttonEditProfile);
        bEditProfile.setOnClickListener(v -> {
            Intent jumpTo = new Intent(getActivity(), ModifyUserAccountActivity.class);
            startActivity(jumpTo);
        });

        // SignOut Button
        final Button buttonLogOut = root.findViewById(R.id.buttonSignOut);
        buttonLogOut.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                FirebaseAuth.getInstance().signOut(); //signout firebase
                Intent jumpTo = new Intent(getActivity(), LoginActivity.class);
                startActivity(jumpTo);
                requireActivity().finish();
                Toast.makeText(getActivity(), "Se ha cerrado sesión.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "Se ha producido un error al cerrar sesión.", Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }

}

