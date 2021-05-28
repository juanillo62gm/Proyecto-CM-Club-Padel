package com.proyectocm.clubpadel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.proyectocm.clubpadel.R;

public class HomeFragment extends Fragment {

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

        return root;
    }

}