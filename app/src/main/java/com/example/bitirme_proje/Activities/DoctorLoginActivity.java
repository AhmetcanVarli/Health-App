package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bitirme_proje.databinding.ActivityDoctorLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

public class DoctorLoginActivity extends AppCompatActivity {

    private ActivityDoctorLoginBinding binding;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    String doctorName;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        setListeners();

    }

    private void setListeners() {
        doctorLogin();
        activityTextInput();
        binding.txtDoctorRegister.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),DoctorRegisterActivity.class));
        });
        binding.btnSignIn.setOnClickListener(view -> getDoctorSignIn());
    }

    private void doctorLogin() {


    }

    private void activityTextInput() {
        binding.inputEmail.getEditText().getText().toString();
    }

    private void getDoctorSignIn() {
        String email = binding.txtinputEmail.getText().toString();
        String password = binding.txtinputPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            binding.txtinputEmail.setError("Email boş olamaz");
            binding.txtinputEmail.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            binding.txtinputPassword.setError("Şifre giriniz");
            binding.txtinputPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(DoctorLoginActivity.this,DoctorHomeActivity.class));
                            }else{
                                Toast.makeText(DoctorLoginActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


}