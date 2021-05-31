package com.example.salehdeliveryproject.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.salehdeliveryproject.databinding.ActivityLoginUserBinding;

public class LoginUserActivity extends AppCompatActivity {
    ActivityLoginUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.setTitle("Login User Screen");

        // Go To Register Screen
        binding.loginUserTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterUserActivity.class);
                startActivity(intent);
            }
        });


        // btn login test   ( i want use API to register and login user)
        binding.loginAdminBtnLoginAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MainUserActivity.class);
                startActivity(intent);
            }
        });


    }
}