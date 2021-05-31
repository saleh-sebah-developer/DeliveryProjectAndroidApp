package com.example.salehdeliveryproject.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityRegisterUsersBinding;
import com.example.salehdeliveryproject.databinding.ActivityRegisterUsersBinding;

public class RegisterUserActivity extends AppCompatActivity {
    ActivityRegisterUsersBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}