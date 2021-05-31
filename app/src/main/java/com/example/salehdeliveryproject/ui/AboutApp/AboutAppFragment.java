package com.example.salehdeliveryproject.ui.AboutApp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.ui.AddNewOrder.AddNewOrderViewModel;

public class AboutAppFragment extends Fragment {

    private AboutAppViewModel aboutAppViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        aboutAppViewModel =
                ViewModelProviders.of(this).get(AboutAppViewModel.class);
        View root = inflater.inflate(R.layout.about_aap, container, false);
        aboutAppViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }
}