package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.bitirme_proje.Activities.HomeActivity;
import com.example.bitirme_proje.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        setListeners();
    }
    void setListeners(){
        binding.txtRegister.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
        });
        binding.btnDoctorSignIn.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),DoctorLoginActivity.class));
        });
        binding.btnSignIn.setOnClickListener(view -> {
            signIn();
        });
        binding.txtForgetPassword.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        });

    }

    private void signIn() {
        String email = binding.txtinputEmail.getText().toString();
        String password = binding.txtinputPassword.getText().toString();

        if (TextUtils.isEmpty(email)){
            binding.txtinputEmail.setError("Email boş olamaz");
            binding.txtinputEmail.requestFocus();

        }else if (TextUtils.isEmpty(password)){
            binding.txtinputPassword.setError("Şifre giriniz");
            binding.txtinputPassword.requestFocus();
        }else{
            db.collection("User")
                    .whereEqualTo("email",email)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                Log.d(TAG, "onComplete: if " + email);
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Log.d(TAG, "onComplete: for");
                                    if (document.getString("email").equals(email)){
                                        Log.d(TAG, "onComplete: ++ "+email);
                                        mAuth.signInWithEmailAndPassword(email,password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()){
                                                            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                                                        }else{
                                                            Toast.makeText(LoginActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(LoginActivity.this, "Kullanıcı Bulunamadı", Toast.LENGTH_SHORT).show();

                                    }
                                }

                            }else{
                                Toast.makeText(LoginActivity.this, "Kullanıcı Yok", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Kullabıcı Yok", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                task.getResult(ApiException.class);
                HomeActivity();

            }catch (ApiException e){
                Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void HomeActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
    }
}