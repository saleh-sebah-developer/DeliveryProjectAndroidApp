package com.example.salehdeliveryproject.ui.AddNewItem;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddNewItemViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddNewItemViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}