package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bitirme_proje.R;
import com.example.bitirme_proje.databinding.ActivityPrescriptionsBinding;
import com.example.bitirme_proje.databinding.AppointmentConfirmBinding;
import com.example.bitirme_proje.databinding.PrescriptionConfirmDialogBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.bitirme_proje.Models.Prescription;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionsActivity extends AppCompatActivity {

    private ActivityPrescriptionsBinding binding;
    private PrescriptionConfirmDialogBinding dialogBinding;
    FirebaseFirestore db;

    List<String> userId = new ArrayList<>();
    List<String> userName = new ArrayList<>();
    List<String> userDate = new ArrayList<>();
    List<String> userTime = new ArrayList<>();

    String userNameTxt;
    String userIDTxt;
    String userDateTxt;
    String userTimeTxt;

    ArrayAdapter<String> dataAdapter;

    int position;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrescriptionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();


        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,userName);

        setListeners();
    }

    private void setListeners() {
        getUserId();
        binding.btnPrescription.setOnClickListener(view -> dialogBuilder());
    }

    private void writePrescription() {

        DoctorHomeActivity doctorHomeActivity = new DoctorHomeActivity();

        String doctorName = doctorHomeActivity.doctorNameSurname;

        String text = binding.txtInputPrescripton.getText().toString();
        String clinicName = doctorHomeActivity.clinicName;

        Log.d(TAG, "writePrescription: " + userNameTxt + " " + userIDTxt +" " + doctorName+ " " +clinicName+" "+text);

        Prescription prescription = new Prescription(userNameTxt,userIDTxt,text,doctorName,clinicName,userDateTxt,userTimeTxt);

        try {
            db.collection("Prescription")
                    .add(prescription)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "onSuccess: ");
                            userId.remove(position);
                            userName.remove(position);
                            userDate.remove(position);
                            userTime.remove(position);
                            Toast.makeText(getApplicationContext(), "Reçete Oluşturuldu", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });
        }catch (Exception e){
            Log.d(TAG, "writePrescription: " + e.getMessage());
        }

    }

    public boolean activityControl(){
        if (binding.txtPatientID.equals(null)){
            return false;
        }else if(binding.txtInputPrescripton.equals(null)){
            return false;
        }else if (position == 0){
            return false;
        }else{
            return true;
        }
    }



    public void getUserId(){

        DoctorHomeActivity doctorHomeActivity = new DoctorHomeActivity();
        Log.d(TAG, "getUserId: " + doctorHomeActivity.doctorNameSurname);

        db.collection("Appointment")
                .whereEqualTo("doctorName",doctorHomeActivity.doctorNameSurname)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                userId.add(document.getString("userID"));
                                userName.add((document.getString("userName") + (document.getString("userSurname"))));
                                userDate.add(document.getString("date"));
                                userTime.add(document.getString("clock"));

                            }
                            userIDSelect();
                            Log.d(TAG, "onComplete: if idler  "+ userId + "\n" + userName + "\n"+ userDate+"\n"+ userTime);
                        }
                    }
                });
    }

    public void userIDSelect(){

        binding.spinnerPatient.setAdapter(dataAdapter);

        binding.spinnerPatient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                position = i;
                for (int x = 0; x < userName.size(); x++){
                    binding.txtPatientID.setText(userId.get(i));
                    binding.txtPatientDate.setText(userDate.get(i));
                    binding.txtPatientTime.setText(userTime.get(i));
                }
                userIDTxt = userId.get(i);
                userNameTxt = userName.get(i);
                userDateTxt = userDate.get(i);
                userTimeTxt = userTime.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });
    }

    private void dialogBuilder(){
        if (activityControl()){
            AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionsActivity.this);
            builder.setTitle("Reçete Onay");

            dialogBinding = PrescriptionConfirmDialogBinding.inflate(getLayoutInflater());

            View dialogLayout = getLayoutInflater().inflate(R.layout.prescription_confirm_dialog,dialogBinding.getRoot());
            builder.setView(dialogLayout);

            AlertDialog dialog = builder.create();

            dialogBinding.btnConfirm.setOnClickListener(view -> {
                loading(true);
                writePrescription();
                finish();
                startActivity(getIntent());
                dialog.dismiss();
            });
            dialogBinding.btnExit.setOnClickListener(view -> dialog.cancel());

            dialog.show();

        }

    }

    private void loading(boolean isLoading){
        if (isLoading){
            dialogBinding.progressBar.setVisibility(View.VISIBLE);
        }else{
            dialogBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PrescriptionsActivity.this,DoctorHomeActivity.class));
    }
}