package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bitirme_proje.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        setListeners();

    }

    private void setListeners() {
        binding.btnSendEmail.setOnClickListener(view -> {
            String userEmail = binding.txtinputEmail.getText().toString();

            if (TextUtils.isEmpty(userEmail)){
                Toast.makeText(this, "Email Giriniz", Toast.LENGTH_LONG).show();
            }else{
                mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Email hesabınızı kontrol ediniz", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                        }else{
                            Toast.makeText(ResetPasswordActivity.this, "Hata" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}