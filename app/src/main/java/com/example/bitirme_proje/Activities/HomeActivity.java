package com.example.bitirme_proje.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bitirme_proje.Utulities.HomeStringData;

import com.example.bitirme_proje.databinding.ActivityHomeBinding;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class HomeActivity extends AppCompatActivity {
//Home Activity
    private ActivityHomeBinding binding;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference docRef;

    private int count = 0;

    List<String> strings;

    public static String userName;
    public static String userSurname;
    String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        binding.textDate.setText(dateFormat.format(date));

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            getUserDetails();
            Log.d(TAG, "onCreate: " + user);
        }

        setListeners();

        strings = HomeStringData.getData();

       // Log.d(TAG, "onCreate: " + strings);


    }
    private void setListeners(){

        binding.imageSettings.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,SettingsActivity.class));
        });
        binding.cardViewPharmacy.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,PharmacyActivity.class));
        });
        binding.cardViewAppointment.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,AppointmentActivity.class));
        });
        binding.carViewAppointmentHistory.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,AppointmentHistoryActivity.class));
        });
        binding.cardViewPrescriptions.setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,PrescriptionHistoryActivity.class));
        });


        Handler handler = new Handler();
        int i = 0;

        Thread thread = new Thread(){

            @Override
            public void run() {
                while (!isInterrupted()){
                    try {
                        Thread.sleep(3000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stringsShow();
                            }
                        });

                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();

        

    }


    private void getUserDetails() {
        docRef = db.collection("User").document(mAuth.getUid());
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            userName = documentSnapshot.getData().get("name").toString();
                            userSurname = documentSnapshot.getData().get("surname").toString();
                            binding.txtName.setText(userName);
                            appointmentShow();
                            Log.d(TAG, "onSuccess: " + userName +" " + userSurname);
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
        Log.d(TAG, "getUserDetails: " + userName);

    }

    private void appointmentShow() {

        Log.d(TAG, "appointmentShow: " + userName);
        db.collection("Appointment")
                .whereEqualTo("userName",userName)
                .whereEqualTo("userSurname",userSurname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Log.d(TAG, "onComplete: " + userName + " " + userSurname);
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

    private void stringsShow() {

        Random random = new Random();
        int i = random.nextInt(strings.size());
        String string = strings.get(i);
        binding.textSwitcher.setText(string);
        //Log.d(TAG, "stringsShow: "+strings.get(i));


    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null){
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }

    }
}