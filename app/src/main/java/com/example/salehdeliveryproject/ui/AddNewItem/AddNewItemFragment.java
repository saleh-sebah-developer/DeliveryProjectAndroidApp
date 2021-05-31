package com.example.salehdeliveryproject.ui.AddNewItem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.salehdeliveryproject.Activitys.AddNewItem;
import com.example.salehdeliveryproject.Activitys.AddNewOrderActivity;
import com.example.salehdeliveryproject.Activitys.MainActivity;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.ui.AddNewOrder.AddNewOrderViewModel;

public class AddNewItemFragment extends Fragment {

    private AddNewItemViewModel addNewItemViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addNewItemViewModel =
                ViewModelProviders.of(this).get(AddNewItemViewModel.class);
        View root = inflater.inflate(R.layout.fragment_view_orders, container, false);
        addNewItemViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Intent intent = new Intent(getContext(), AddNewItem.class);
                startActivity(intent);
            }
        });
        return root;
    }

}