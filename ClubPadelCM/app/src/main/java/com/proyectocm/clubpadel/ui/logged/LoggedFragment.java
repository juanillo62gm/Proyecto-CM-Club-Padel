package com.proyectocm.clubpadel.ui.logged;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.proyectocm.clubpadel.R;

public class LoggedFragment extends Fragment {

    private LoggedViewModel loggedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loggedViewModel =
                new ViewModelProvider(this).get(LoggedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logged, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        loggedViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }


}