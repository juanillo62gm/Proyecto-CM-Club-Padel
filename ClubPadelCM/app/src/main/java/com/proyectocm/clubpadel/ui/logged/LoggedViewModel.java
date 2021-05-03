package com.proyectocm.clubpadel.ui.logged;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoggedViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public LoggedViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Este es el perfil con la sesión iniciada");
    }

    public LiveData<String> getText() {
        return mText;
    }
}