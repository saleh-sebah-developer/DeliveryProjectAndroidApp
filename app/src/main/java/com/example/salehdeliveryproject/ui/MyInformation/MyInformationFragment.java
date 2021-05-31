package com.example.salehdeliveryproject.ui.MyInformation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.salehdeliveryproject.R;

public class MyInformationFragment extends Fragment {

    private MyInformationViewModel myInformationViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myInformationViewModel =
                ViewModelProviders.of(this).get(MyInformationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_information, container, false);
        myInformationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}