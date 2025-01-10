package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bitirme_proje.Models.Doctor;
import com.example.bitirme_proje.Utulities.Clinik;
import com.example.bitirme_proje.R;
import com.example.bitirme_proje.databinding.ActivityDoctorRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class DoctorRegisterActivity extends AppCompatActivity {

    private ActivityDoctorRegisterBinding binding;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;

    Clinik clinik = new Clinik();

    List<String> spinnerData = new ArrayList<>();
    ArrayAdapter<String> dataAdapter;

    String clinicName;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerData);


        setListeners();


    }

    private void setListeners() {
        clinicJSONList();
        clinicSelect();
        binding.btnRegister.setOnClickListener(view -> createDoctor());
    }

    private void createDoctor() {
        String doctorName = binding.textInputName.getText().toString().toUpperCase();
        String doctorSurname = binding.textInputSurname.getText().toString().toUpperCase();
        String doctorEmail = binding.textInputEmail.getText().toString();
        String doctorPassword = binding.textInputPassword.getText().toString();
        String doctorConfirmPassword = binding.textInputCheckPassword.getText().toString();


        if (TextUtils.isEmpty((doctorName))){
            binding.textInputName.setError("TC Kimlik No boş olamaz");
            binding.textInputName.requestFocus();

        }
        else if(TextUtils.isEmpty(doctorSurname)){
            binding.textInputSurname.setError("Ad boş olamaz");
            binding.textInputSurname.requestFocus();

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(doctorEmail).matches()){
            binding.textInputEmail.setError("Geçerli email giriniz");
            binding.textInputEmail.requestFocus();
            Log.d(TAG, "createDoctor: " + doctorName + doctorSurname);

        }else if(TextUtils.isEmpty(doctorPassword)){
            binding.textInputPassword.setError("Şifre boş olamaz");
            binding.textInputPassword.requestFocus();

        }
        else if(TextUtils.isEmpty(doctorConfirmPassword)){
            binding.textInputCheckPassword.setError("Sifre Onayla boş olamaz");
            binding.textInputCheckPassword.requestFocus();

        } else if(!doctorPassword.equals((doctorConfirmPassword))){
            binding.textInputCheckPassword.setError("Şifreler uyuşmuyor");
            binding.textInputCheckPassword.requestFocus();

        }
        else{
            mAuth.createUserWithEmailAndPassword(doctorEmail,doctorPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){

                        Doctor doctor = new Doctor(doctorName,clinicName,doctorPassword,doctorEmail,doctorSurname);

                        db.collection("Doctors").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .set(doctor)
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

                        startActivity(new Intent(DoctorRegisterActivity.this,DoctorHomeActivity.class));

                        Toast.makeText(DoctorRegisterActivity.this, "Kayıt Başarılı", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(DoctorRegisterActivity.this, "Kayıt HATA: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }



    }


    private void clinicJSONList() {
        try {
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.klinik)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null;){
                jsonBuilder.append(line).append("\n");
            }
            Gson gson = new Gson();
            clinik = gson.fromJson(jsonBuilder.toString(), Clinik.class);
        }catch (FileNotFoundException e){
            Log.d(TAG, "clinicJSONList: " +e.getMessage());
        }catch (IOException e){
            Log.d(TAG, "clinicJSONList: " + e.getMessage());
        }
    }

    private void clinicSelect() {

        for (int i = 0; i < clinik.getData().size(); i++) {
            spinnerData.add(clinik.getData().get(i).getKlinikAdi());
        }
        binding.clinicSpinner.setAdapter(dataAdapter);
        binding.clinicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clinicName = binding.clinicSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}