package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bitirme_proje.Adapters.AppointmentDoctorAdapter;
import com.example.bitirme_proje.Models.Appointment;
import com.example.bitirme_proje.databinding.ActivityDoctorAppointmentHistoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DoctorAppointmentHistoryActivity extends AppCompatActivity {

    ActivityDoctorAppointmentHistoryBinding binding;
    private FirebaseFirestore db;

    List<Appointment> appointmentList = new ArrayList<>();
    AppointmentDoctorAdapter appointmentDoctorAdapter = new AppointmentDoctorAdapter(appointmentList);

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDoctorAppointmentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        setListeners();
    }


    private void setListeners() {
        getAppointmentDoctor();
    }

    private void getAppointmentDoctor() {
        DoctorHomeActivity doctorHomeActivity = new DoctorHomeActivity();
        loading(true);
        Log.d(TAG, "getAppointment: " + doctorHomeActivity.doctorNameSurname);
        db.collection("Appointment")
                .whereEqualTo("doctorName",doctorHomeActivity.doctorNameSurname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loading(false);
                        if (task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: girdi");
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Appointment appointment = new Appointment();

                                appointment.userName = document.getString("userName");
                                appointment.userSurname = document.getString("userSurname");
                                appointment.doctorName = document.getString("doctorName");
                                appointment.date = document.getString("date");
                                appointment.clock = document.getString("clock");

                                appointmentList.add(appointment);
                            }
                            if (appointmentList.size() > 0){

                                Collections.sort(appointmentList, new Comparator<Appointment>() {
                                    @Override
                                    public int compare(Appointment appointment, Appointment t1) {

                                        int res = String.CASE_INSENSITIVE_ORDER.compare(t1.date,appointment.date);
                                        return res;
                                    }
                                });

                                binding.recyclerView.setHasFixedSize(true);
                                binding.recyclerView.setAdapter(appointmentDoctorAdapter);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(DoctorAppointmentHistoryActivity.this));
                                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());
                            }else{
                                Toast.makeText(DoctorAppointmentHistoryActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }


    private void loading(boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DoctorAppointmentHistoryActivity.this,DoctorHomeActivity.class));
    }
}