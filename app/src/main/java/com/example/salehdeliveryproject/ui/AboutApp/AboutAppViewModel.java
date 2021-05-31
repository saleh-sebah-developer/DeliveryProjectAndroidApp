package com.example.salehdeliveryproject.ui.AboutApp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutAppViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AboutAppViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}