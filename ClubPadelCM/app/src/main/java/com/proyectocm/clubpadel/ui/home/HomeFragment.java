package com.proyectocm.clubpadel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.proyectocm.clubpadel.Booking;
import com.proyectocm.clubpadel.BookingActivity;
import com.proyectocm.clubpadel.LoginActivity;
import com.proyectocm.clubpadel.R;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button booking_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        booking_button = root.findViewById(R.id.booking_button_home);
        booking_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent jumpTo = new Intent(getActivity(), BookingActivity.class);
                startActivity(jumpTo);
                getActivity().finish();
            }
        });
        return root;
    }
}