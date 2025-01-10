package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bitirme_proje.Models.Prescription;
import com.example.bitirme_proje.Adapters.PrescriptionAdapter;
import com.example.bitirme_proje.Listeners.PrescriptionListener;
import com.example.bitirme_proje.R;
import com.example.bitirme_proje.databinding.ActivityPrescriptionHistoryBinding;
import com.example.bitirme_proje.databinding.ItemPrescriptionContentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.app.Presentation;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PrescriptionHistoryActivity extends AppCompatActivity implements PrescriptionListener{
    
    private ActivityPrescriptionHistoryBinding binding;
    private FirebaseFirestore db;

    ItemPrescriptionContentBinding dialogbinding;

    List<Prescription> prescriptionList = new ArrayList<>();
    PrescriptionAdapter prescriptionAdapter = new PrescriptionAdapter(prescriptionList,this);

    String text ;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrescriptionHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = FirebaseFirestore.getInstance();

        Prescription prescription1 = new Prescription();

        Log.d(TAG, "onCreate: " + prescription1.userPrescription);

        setListeners();
    }

    private void setListeners() {
        getPrenscription();
    }

    private void getPrenscription() {
        HomeActivity homeActivity = new HomeActivity();
        loading(true);
        try {
            Log.d(TAG, "getPrenscription: isim = " + homeActivity.userName +" " + homeActivity.userSurname);
            db.collection("Prescription")
                    .whereEqualTo("userName",homeActivity.userName  + homeActivity.userSurname)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            loading(false);
                            if (task.isSuccessful() && task.getResult() != null){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Prescription prescription = new Prescription() ;

                                    prescription.clinicName = document.getString("clinicName");
                                    prescription.doctorName = document.getString("doctorName");
                                    prescription.date = document.getString("date");
                                    prescription.time = document.getString("time");
                                    prescription.userPrescription = document.getString("userPrescription");

                                    prescriptionList.add(prescription);
                                }
                                if (prescriptionList.size() > 0){
                                    Log.d(TAG, "onComplete: ife girdi");



                                    binding.recyclerView.setHasFixedSize(true);
                                    binding.recyclerView.setAdapter(prescriptionAdapter);
                                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(PrescriptionHistoryActivity.this));
                                    binding.recyclerView.setItemAnimator(new DefaultItemAnimator());

                                }else{
                                    Toast.makeText(PrescriptionHistoryActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "getPrenscription: Hata mesajı" + e.getMessage());
        }


    }

    private void dialogBuilder(Prescription prescription){
        AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionHistoryActivity.this);
        //builder.setTitle("Reçete");
        TextView textView = new TextView(this);
        textView.setText("Reçete");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setTextSize(30);
        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
        textView.setTextColor(Color.BLACK);

        builder.setCustomTitle(textView);

        dialogbinding = ItemPrescriptionContentBinding.inflate(getLayoutInflater());

        View dialogLayout = getLayoutInflater().inflate(R.layout.item_prescription_content,dialogbinding.getRoot());
        builder.setView(dialogLayout);

        dialogbinding.prescriptonClinicName.setText(prescription.clinicName);
        dialogbinding.prescriptonDoctorName.setText(prescription.doctorName);
        dialogbinding.prescriptonDate.setText(prescription.date);
        dialogbinding.prescriptonTime.setText(prescription.time);
        dialogbinding.prescriptonText.setText(prescription.userPrescription);
        Log.d(TAG, "dialogBuilder: " + prescription.userPrescription);
        Log.d(TAG, "dialogBuilder: " + prescription.doctorName);


        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loading(boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPrescriptionClicked(Prescription prescription) {

        /*db.collection("Prescription")
                .whereEqualTo("clinicName",prescription.clinicName)
                .whereEqualTo("doctorName",prescription.doctorName)
                .whereEqualTo("date",prescription.date)
                .whereEqualTo("time",prescription.time)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                text = document.getString("userPrescription").toString();
                            }
                            Log.d(TAG, "onComplete: texttt " + text);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });*/
        Log.d(TAG, "onPrescriptionClicked: " + prescription.userPrescription);
        dialogBuilder(prescription);
    }
}