package com.example.salehdeliveryproject.ui.AddNewOrder;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.salehdeliveryproject.Activitys.AddNewItem;
import com.example.salehdeliveryproject.Activitys.AddNewOrderActivity;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.modles.Order;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddNewOrderFragment extends Fragment {

    private AddNewOrderViewModel addNewOrderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addNewOrderViewModel =
                ViewModelProviders.of(this).get(AddNewOrderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_view_orders, container, false);
        addNewOrderViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Intent intent = new Intent(getContext(), AddNewOrderActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }

}