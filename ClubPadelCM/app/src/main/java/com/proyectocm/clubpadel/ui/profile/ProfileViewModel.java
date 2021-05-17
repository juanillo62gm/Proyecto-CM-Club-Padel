package com.proyectocm.clubpadel.ui.profile;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el perfil sin iniciar sesión");
    }

    public  LiveData<String> getText() {
        return mText;
    }
}