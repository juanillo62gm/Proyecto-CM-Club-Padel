package com.proyectocm.clubpadel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.proyectocm.clubpadel.BookingActivity;
import com.proyectocm.clubpadel.LoginActivity;
import com.proyectocm.clubpadel.MyBookingsActivity;
import com.proyectocm.clubpadel.R;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        new ViewModelProvider(this).get(HomeViewModel.class);
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

        // Test Button
        final Button buttonLogOut = root.findViewById(R.id.buttonTest);
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