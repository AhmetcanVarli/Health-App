package com.example.bitirme_proje.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bitirme_proje.databinding.ActivitySettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private ActivitySettingsBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        setListeners();
    }

    private void setListeners() {
        binding.btnSignOut.setOnClickListener(view -> {
            signOut();
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(SettingsActivity.this,LoginActivity.class));

    }
}