package com.example.salehdeliveryproject.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.salehdeliveryproject.databinding.ActivityOtherShposBinding;

public class OtherShopsActivity extends AppCompatActivity {
    ActivityOtherShposBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtherShposBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}