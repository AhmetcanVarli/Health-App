package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.bitirme_proje.Adapters.ExpandListViewAdapter;
import com.example.bitirme_proje.R;
import com.example.bitirme_proje.Models.Appointment;
import com.example.bitirme_proje.Utulities.ExpandableListDataPump;
import com.example.bitirme_proje.Models.Doctor;
import com.example.bitirme_proje.databinding.ActivityClockSelectBinding;
import com.example.bitirme_proje.databinding.AppointmentConfirmBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClockSelectActivity extends AppCompatActivity {

    ActivityClockSelectBinding binding;
    AppointmentConfirmBinding bindingDialog;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference dbRef;

    AppointmentActivity appointmentActivity = new AppointmentActivity();

    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;
    HashMap<String, List<String>> expandableListDetailNew;

    List<String> clockList = new ArrayList<String>();



    public static String time;
    public static String userName;
    public static String userSurname;
    public static String userID;
    String clinikName;
    String doctorName;
    String date;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockSelectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        db = FirebaseFirestore.getInstance();

        getClockDetails();

        Log.d(TAG, "getClockDetails: " + clockList);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandListViewAdapter(this, expandableListTitle, expandableListDetail);
        binding.expandableListView.setAdapter(expandableListAdapter);
        Collections.sort(expandableListTitle);
        Log.d(TAG, "onCreate: timeee" + time);

        binding.expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        binding.expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        binding.expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                Object obj = parent.getTag();
                if (obj instanceof  View){
                    ((View) obj).findViewById(R.id.expandedListItem).setBackgroundColor(Color.WHITE);
                }
                v.findViewById(R.id.expandedListItem).setBackgroundColor(Color.RED);
                parent.setTag(v);

                time = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).toString();
                Log.d(TAG, "onChildClick: time " + time);
                return false;
            }
        });

        setListeners();


    }

    private void setListeners() {

        getUserDetails();
        binding.btnDone.setOnClickListener(view -> dialogBuilder());
    }

    private void appointmentSave() {

        Log.d(TAG, "appointmentSave: " + time);

        Log.d(TAG, "getUserDetails: " +appointmentActivity.clinicName +" - " + appointmentActivity.doctorName +" - " + appointmentActivity.date + " - " + userName + " - " + userSurname + " - " + time);

        Doctor doctor = new Doctor(appointmentActivity.doctorName,appointmentActivity.date,time,appointmentActivity.clinicName);


        Appointment appointment = new Appointment(appointmentActivity.clinicName,appointmentActivity.doctorName,appointmentActivity.date,time,userName,userSurname,userID);

        db.collection("Appointment")
                .add(appointment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: ");
                        Toast.makeText(appointmentActivity, "Randevunuz Oluşturuldu", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        db.collection("Doctors")
                .add(doctor)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        loading(false);

        //Toast.makeText(this, "time :" + time, Toast.LENGTH_SHORT).show();*/


    }

    private boolean activityControl() {
        if (time == null){
            Toast.makeText(ClockSelectActivity.this, "Saat seçiniz", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void getUserDetails() {

        HomeActivity homeActivity = new HomeActivity();
        mAuth = homeActivity.mAuth;
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "getUserDetails: " + mAuth.getUid());
        
        dbRef = db.collection("User").document(mAuth.getUid());
        dbRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                     if (documentSnapshot.exists()) {
                         userName = documentSnapshot.get("name").toString();
                         userSurname = documentSnapshot.get("surname").toString();
                         userID = documentSnapshot.get("id").toString();
                         Log.d(TAG, "onSuccess: " + userName+userSurname);
                     }else{
                         Log.d(TAG, "onSuccess: Hata");
                     }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: HATA");
                    }
                });


    }

    private void getClockDetails(){
        Log.d(TAG, "getClockDetails: " + clockList);
        db.collection("Appointment")
                .whereEqualTo("doctorName",appointmentActivity.doctorName)
                .whereEqualTo("date",appointmentActivity.date)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                Log.d(TAG, "onComplete: " + appointmentActivity.doctorName+ " " + document.get("clock"));
                                String clock = document.get("clock").toString();
                                clockList.add(clock);
                            }

                            expandableListClock();
                            Log.d(TAG, "onComplete: " + clockList);

                        }
                    }
                });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void expandableListClock() {

        expandableListDetail.forEach((key,value) ->{
         //   Log.d(TAG, "expandableListClock: forEach " + value);
           // Log.d(TAG, "expandableListClock: forEach " + clockList);
            Iterator<String> itr = value.iterator();
            while (itr.hasNext()){
                String clock = itr.next();
                if (clockList.contains(clock)){
                    itr.remove();
                }
            }
            Log.d(TAG, "expandableListClock: value " + value);
        });
    }

    private void dialogBuilder(){
        if (activityControl()){
            AlertDialog.Builder builder =new AlertDialog.Builder(ClockSelectActivity.this);
            builder.setTitle("Randevu Onay");



            bindingDialog = AppointmentConfirmBinding.inflate(getLayoutInflater());

            View dialogLayout = getLayoutInflater().inflate(R.layout.appointment_confirm,bindingDialog.getRoot());
            builder.setView(dialogLayout);

            bindingDialog.textClinicName.setText(appointmentActivity.clinicName);
            bindingDialog.textDoctorName.setText(appointmentActivity.doctorName);
            bindingDialog.textDoctorName.setText(appointmentActivity.doctorName);
            bindingDialog.textDate.setText(appointmentActivity.date);
            bindingDialog.textTime.setText(time);

            AlertDialog dialog = builder.create();

            bindingDialog.btnConfirm.setOnClickListener(view ->{
                loading(true);
                appointmentSave();
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                dialog.dismiss();

            } );
            bindingDialog.btnExit.setOnClickListener(view -> {
                dialog.cancel();
            });

            dialog.show();

        }

    }

    private void loading(boolean isLoading){
        if (isLoading){
            bindingDialog.progressBar.setVisibility(View.VISIBLE);
        }else{
            bindingDialog.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}