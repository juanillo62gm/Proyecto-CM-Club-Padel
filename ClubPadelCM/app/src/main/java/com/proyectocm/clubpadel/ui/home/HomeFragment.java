package com.proyectocm.clubpadel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.proyectocm.clubpadel.BookingActivity;
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
        return root;
    }
}