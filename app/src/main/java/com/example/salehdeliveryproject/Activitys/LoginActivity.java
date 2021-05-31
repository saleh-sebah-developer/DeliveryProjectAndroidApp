package com.example.salehdeliveryproject.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.setTitle("Welcome");

        //User
        binding.loginBtnLoginAsUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),LoginUserActivity.class);
                startActivity(intent);
            }
        });

        // Admin
        binding.loginBtnLoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),LoginAdminActivity.class);
                startActivity(intent);
            }
        });

        // Delivery
        binding.loginBtnLoginAsDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(5)
                        .playOn(binding.loginBtnLoginAsDelivery);
               // Intent intent = new Intent(getBaseContext(),LoginAdminActivity.class);
              //  startActivity(intent);
            }
        });

    }
}