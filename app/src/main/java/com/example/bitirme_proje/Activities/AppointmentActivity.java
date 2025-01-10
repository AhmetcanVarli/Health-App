package com.example.bitirme_proje.Activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.bitirme_proje.databinding.ActivityAppointmentBinding;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.bitirme_proje.Models.Clinic;
import com.example.bitirme_proje.Utulities.Clinik;
import com.example.bitirme_proje.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AppointmentActivity extends AppCompatActivity {

    private ActivityAppointmentBinding binding;
    Clinik clinik = new Clinik();
    

    List<Clinic> clinics = new ArrayList<>();
    List<String> spinnerData = new ArrayList<>();
    List<String> spinnerData2 = new ArrayList<>();
    List<String> spinnerData3 = new ArrayList<>();


    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> dataAdapter2;
    ArrayAdapter<String> dataAdapter3;

    int position;
    int position2;

    public static String doctorName;
    public static String clinicName;
    public static String date;
    public static String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerData);
        dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,spinnerData2);
        dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1,spinnerData3);



        

        setListeners();


    }


    private void setListeners() {
        binding.btnDateTxt.setOnClickListener(view -> dataPicker());
        binding.btnNext.setOnClickListener(view -> {
            if(activityControl()) {
                startActivity(new Intent(AppointmentActivity.this, ClockSelectActivity.class));
            }
           // Log.d(TAG, "onItemSelected: " + clinicName +" " + doctorName + " " + date);
        });
        clinicJSONList();
        clinicSelect();


    }



    private void dataPicker() {

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH ) ;
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(AppointmentActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i2+"/"+(i1+1)+"/"+i;

                binding.btnDateTxt.setText(date);
            }
        },year,month ,day);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }

    private boolean activityControl() {
        String spinnerClicicText = binding.spinnerClinic.getSelectedItem().toString();
        String spinnerDoctorText = binding.spinnerDoctor.getSelectedItem().toString();
        if(spinnerClicicText.equals("Algoloji")){
            Toast.makeText(this, "Klinik Seçin", Toast.LENGTH_SHORT).show();
            return false;
        }else if(spinnerDoctorText.equals("SEVİNÇ KÜÇÜKDEMİRTAŞ")){
            Toast.makeText(this, "Doktor Seçin", Toast.LENGTH_SHORT).show();
            return false;
        }else if(binding.btnDateTxt.getText().equals("-- -- --")){
            Toast.makeText(this, "Tarih Seçin", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    private void materialDataPicker() {
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Tarih").build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                binding.btnDateTxt.setText(sdf.format(datePicker.getSelection()));
            }
        });
        datePicker.show(getSupportFragmentManager(),"TAG");
    }


    private void clinicJSONList() {
       try {
           BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.klinik)));
           StringBuilder jsonBuilder = new StringBuilder();
           for (String line = null; (line = jsonReader.readLine()) != null;){
               jsonBuilder.append(line).append("\n");
           }
           Gson gson = new Gson();
           clinik = gson.fromJson(jsonBuilder.toString(),Clinik.class);
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
        binding.spinnerClinic.setAdapter(dataAdapter);
        binding.spinnerClinic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                spinnerData2.clear();
                for (int x = 0; x < clinik.getData().get(i).getDoktorlar().size(); x++) {
                   // Log.d(TAG, "onItemSelected: " + clinik.getData().get(i).getDoktorlar().get(x).getDoktorAdi());
                    spinnerData2.add(clinik.getData().get(i).getDoktorlar().get(x).getDoktorAdi());

                }
                binding.spinnerDoctor.setAdapter(dataAdapter2);

                clinicName = binding.spinnerClinic.getSelectedItem().toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                doctorName = spinnerData2.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
}