package com.example.salehdeliveryproject.ui.MyInformation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyInformationViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyInformationViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}