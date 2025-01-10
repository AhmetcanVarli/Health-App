package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bitirme_proje.Models.Appointment;
import com.example.bitirme_proje.Adapters.AppointmentAdapter;
import com.example.bitirme_proje.databinding.ActivityAppointmentHistoryBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppointmentHistoryActivity extends AppCompatActivity {

    private ActivityAppointmentHistoryBinding binding;
    private FirebaseFirestore db;

    List<Appointment> appointmentList = new ArrayList<>();
    AppointmentAdapter appointmentAdapter = new AppointmentAdapter(appointmentList);

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        setListeners();
    }

    private void setListeners() {
        getAppointment();
    }

    public void getAppointment(){
        HomeActivity homeActivity = new HomeActivity();
        loading(true);
        Log.d(TAG, "getAppointment: " + homeActivity.userName + " " + homeActivity.userSurname);
        db.collection("Appointment")
                .whereEqualTo("userName",homeActivity.userName)
                .whereEqualTo("userSurname",homeActivity.userSurname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loading(false);
                        if (task.isSuccessful() && task.getResult() != null){
                            Log.d(TAG, "onComplete: girdi");
                            for (QueryDocumentSnapshot document : task.getResult()){
                                Appointment appointment = new Appointment();

                                Log.d(TAG, "onComplete: fora girdi");
                                appointment.clinicName = document.getString("clinicName");
                                appointment.doctorName = document.getString("doctorName");
                                appointment.date = document.getString("date");
                                appointment.clock = document.getString("clock");

                                appointmentList.add(appointment);
                            }
                            if (appointmentList.size() > 0){
                                Log.d(TAG, "onComplete: ife girdi");

                                Collections.sort(appointmentList, new Comparator<Appointment>() {
                                    @Override
                                    public int compare(Appointment appointment, Appointment t1) {
                                        int res = String.CASE_INSENSITIVE_ORDER.compare( t1.date,appointment.date);
                                        return res;
                                    }
                                });


                                binding.recyclerView.setHasFixedSize(true);
                                binding.recyclerView.setAdapter(appointmentAdapter);
                                binding.recyclerView.setLayoutManager(new LinearLayoutManager(AppointmentHistoryActivity.this));
                                binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

                            }else{
                                Toast.makeText(AppointmentHistoryActivity.this, "Hata", Toast.LENGTH_SHORT).show();
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
}