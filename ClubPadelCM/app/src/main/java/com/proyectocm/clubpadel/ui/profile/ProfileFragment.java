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
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.proyectocm.clubpadel.LoginActivity;
import com.proyectocm.clubpadel.R;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;

    // Firebase RealtimeDatabase
    private DatabaseReference mReference;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mReference = FirebaseDatabase.getInstance("https://club-padel-cm-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final TextView dataName = root.findViewById(R.id.fetchName);
        final TextView dataSurname = root.findViewById(R.id.fetchSurname);
        final TextView dataPhone = root.findViewById(R.id.fetchPhone);
        final TextView dataUserEmail = root.findViewById(R.id.fetchEmail);

        // Request stored info to DB
        mReference.child("Users")
                .child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String fbName = dataSnapshot.child("name").getValue().toString();
                        String fbSurname = dataSnapshot.child("surname").getValue().toString();
                        String fbPhone = dataSnapshot.child("phone").getValue().toString();
                        String fbEmail = dataSnapshot.child("email").getValue().toString();
                        dataName.setText(fbName);
                        dataSurname.setText(fbSurname);
                        dataPhone.setText(fbPhone);
                        dataUserEmail.setText(fbEmail);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });

        // SignOut Button
        final Button buttonLogOut = root.findViewById(R.id.signOut);
        buttonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    FirebaseAuth.getInstance().signOut(); //signout firebase
                    Intent jumpTo = new Intent(getActivity(), LoginActivity.class);
                    startActivity(jumpTo);
                    getActivity().finish();
                    Toast.makeText(getActivity(), "Se ha cerrado sesión.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "Se ha producido un error al cerrar sesión.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return root;
    }
}

