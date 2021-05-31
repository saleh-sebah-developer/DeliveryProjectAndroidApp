package com.example.salehdeliveryproject.ui.AddNewOrder;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddNewOrderViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddNewOrderViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}