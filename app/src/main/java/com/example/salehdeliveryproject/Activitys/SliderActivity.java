package com.example.salehdeliveryproject.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.salehdeliveryproject.Adapters.AdapterSlide;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivitySliderBinding;

public class SliderActivity extends AppCompatActivity {
    ActivitySliderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySliderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AdapterSlide adapterSlide = new AdapterSlide(this);
        binding.viewpager.setAdapter(adapterSlide);


    }
}