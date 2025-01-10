package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.bitirme_proje.Models.User;
import com.example.bitirme_proje.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DatabaseReference dbRef;

    Boolean gender;
    String TAG ="TAG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        dbRef = FirebaseDatabase.getInstance().getReference();


        Log.d(TAG, "onCreate: ");
        Log.w(TAG, "onCreate: " );

        setListeners();

    }

    private void setListeners() {
        binding.btnRegister.setOnClickListener(view -> createUser());
        genderContorl();
    }

    private void createUser() {
        String id = binding.textInputIDNo.getText().toString();
        String name = binding.textInputName.getText().toString();
        String surname = binding.textInputSurname.getText().toString();
        String email = binding.textInputEmail.getText().toString();
        String phoneNo = binding.textInputPhoneNumber.getText().toString();
        String password = binding.textInputPassword.getText().toString();
        String checkPassword = binding.textInputCheckPassword.getText().toString();


        if (TextUtils.isEmpty((id))){
            binding.textInputIDNo.setError("TC Kimlik No boş olamaz");
            binding.textInputIDNo.requestFocus();

        }
        else if(TextUtils.isEmpty(name)){
            binding.textInputName.setError("Ad boş olamaz");
            binding.textInputName.requestFocus();

        }
        else if(TextUtils.isEmpty(surname)){
            binding.textInputSurname.setError("Soyad boş olamaz");
            binding.textInputSurname.requestFocus();

        }
        else if(TextUtils.isEmpty(email)){
            binding.textInputEmail.setError("Email boş olamaz");
            binding.textInputEmail.requestFocus();

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.textInputEmail.setError("Geçerli email giriniz");
            binding.textInputEmail.requestFocus();

        }
        else if(gender == null){
            Toast.makeText(this, "Cinsiyet Seçin ", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(phoneNo)){
            binding.textInputPhoneNumber.setError("Telefon NO boş olamaz");
            binding.textInputPhoneNumber.requestFocus();

        }
        else if(TextUtils.isEmpty(password)){
            binding.textInputPassword.setError("Şifre boş olamaz");
            binding.textInputPassword.requestFocus();

        }
        else if(TextUtils.isEmpty(checkPassword)){
            binding.textInputCheckPassword.setError("Sifre Onayla boş olamaz");
            binding.textInputCheckPassword.requestFocus();

        } else if(!password.equals((checkPassword))){
            binding.textInputCheckPassword.setError("Şifreler uyuşmuyor");
            binding.textInputCheckPassword.requestFocus();

        }
        else{
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        User admin = new User(id,name,surname,phoneNo,email,password,gender);

                        db.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(admin)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG, "onSuccess: " + unused);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "onFailure: FAILLLL");
                                    }
                                });

                        startActivity(new Intent(RegisterActivity.this,HomeActivity.class));

                        Toast.makeText(RegisterActivity.this, "User Registered is Successfully", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


    }



    private void genderContorl(){
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{-android.R.attr.state_enabled}
                },
                new int[]{
                        Color.rgb(64,64,64),
                        Color.rgb(230,82,29)
                }
        );
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(binding.radioBtnMan.getId() == i){
                    binding.radioBtnMan.setButtonTintList(colorStateList);
                    gender = true;
                }else if(binding.radioBtnWoman.getId() == i){
                    binding.radioBtnWoman.setButtonTintList(colorStateList);
                    gender = false;
                }
            }
        });
    }


}