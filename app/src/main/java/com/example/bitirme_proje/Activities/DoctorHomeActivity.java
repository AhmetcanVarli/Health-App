package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.bitirme_proje.databinding.ActivityDoctorHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DoctorHomeActivity extends AppCompatActivity {

    private ActivityDoctorHomeBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    private int count = 0;



    public static String doctorName;
    public static String doctorSurname;
    public static String doctorNameSurname;
    public static String clinicName;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        binding.textDate.setText(dateFormat.format(date));

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            getUserDetails();
        }

        setListeners();
    }

    private void setListeners() {
        binding.imageSignOut.setOnClickListener(view -> signOut());
        binding.carViewAppointmentHistory.setOnClickListener(view -> {
            startActivity(new Intent(DoctorHomeActivity.this,DoctorAppointmentHistoryActivity.class));
        });
        binding.cardViewPrescriptions.setOnClickListener(view -> {
            startActivity(new Intent(DoctorHomeActivity.this,PrescriptionsActivity.class));
        });
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DoctorHomeActivity.this,LoginActivity.class));

    }

    private void getUserDetails() {
        docRef = db.collection("Doctors").document(mAuth.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            doctorName = documentSnapshot.getData().get("doctorName").toString();
                            doctorSurname = documentSnapshot.getData().get("doctorSurname").toString();
                            clinicName = documentSnapshot.getData().get("clinicName").toString();
                            binding.txtName.setText(doctorName);
                            appointmentShow();
                            Log.d(TAG, "onSuccess: " + doctorName +" " + doctorSurname);
                        }else{
                            Log.d(TAG, "onSuccess: hata yok");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.txtName.setText("Hata");
                    }
                });


    }

    private void appointmentShow() {

        doctorNameSurname = doctorName + " " + doctorSurname;
        Log.d(TAG, "appointmentShow: nameSurname = " + doctorNameSurname);

        db.collection("Appointment")
                .whereEqualTo("doctorName",doctorNameSurname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Log.d(TAG, "onComplete: " + doctorName + " " + doctorSurname);
                                count++;
                            }
                            if(count > 0){
                                binding.txtAppointment.setText(count + " tane randevunuz var");
                            }else{
                                binding.txtAppointment.setText("Randevunuz Yok");
                            }
                        }
                    }
                });

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        Log.d(TAG, "onStart: DoctorHome" + mAuth);

        if (user == null){
            startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
        }
    }
}