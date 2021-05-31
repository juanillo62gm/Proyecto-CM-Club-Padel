package com.proyectocm.clubpadel.ui.home;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.proyectocm.clubpadel.R;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        Button booking_button = root.findViewById(R.id.buttonBooking);
        booking_button.setOnClickListener(v -> {
            Intent jumpTo = new Intent(getActivity(), BookingActivity.class);
            startActivity(jumpTo);
        });

        Button my_bookings = root.findViewById(R.id.buttonMyBookings);
        my_bookings.setOnClickListener(v -> {
            Intent jumpTo = new Intent(getActivity(), MyBookingsActivity.class);
            startActivity(jumpTo);

        });
        final TextView textView = root.findViewById(R.id.textWelcome);
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DocumentReference docRef = db.collection("Users").document(userId);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (Objects.requireNonNull(document).exists()) {
                    String fbName = Objects.requireNonNull(document.get("name")).toString();
                    textView.setText("Hola " + fbName + ",");
                } else {
                    Toast.makeText(getActivity(), "No existe el usuario", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getActivity(), "Error en la solicitud" + task.getException(), Toast.LENGTH_LONG).show();
            }
        });


        return root;
    }

}