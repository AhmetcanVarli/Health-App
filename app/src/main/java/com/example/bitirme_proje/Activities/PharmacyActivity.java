package com.example.bitirme_proje.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.bitirme_proje.R;
import com.example.bitirme_proje.Utulities.*;
import com.example.bitirme_proje.Listeners.*;
import com.example.bitirme_proje.Models.Pharmacy;
import com.example.bitirme_proje.Adapters.PharmacyAdapter;
import com.example.bitirme_proje.databinding.ActivityPharmacyBinding;
import com.example.bitirme_proje.databinding.ItemContainerPharmacyBinding;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PharmacyActivity extends AppCompatActivity implements PharmacyListener {
    private ActivityPharmacyBinding binding;

    CityPharmacy cityPharmacy = new CityPharmacy();
    private ItemContainerPharmacyBinding pharmacyBindig;


    List<Pharmacy> pharmacies = new ArrayList<>();
    List<String> spinnerData = new ArrayList<>();
    List<String> spinnerData2 = new ArrayList<>();
    List<String> spinnerData3 = new ArrayList<>();

    PharmacyAdapter pharmacyAdapter = new PharmacyAdapter(pharmacies, this);

    ArrayAdapter<String> dataAdapter;
    ArrayAdapter<String> dataAdapter2;
    ArrayAdapter<String> dataAdapter3;
    int position;
    int position2;
    String pharmacyName;
    String pharmacyAdress;
    String pharmacyPhone;
    String pharmacyLatLng;

    String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPharmacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerData);
        dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerData2);
        dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, spinnerData3);


        setListeners();


    }

    private void setListeners() {
        cityJSONList();
        districtSelect();

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                String city = hereLocation(location.getLatitude(), location.getLongitude());
                Log.d(TAG, "setListeners: cityName" + city);
            } catch (Exception e) {
                Log.d(TAG, "setListeners: " + e.getMessage());
            }

        }*/

        binding.btnGoster.setOnClickListener(view -> {

            Log.d(TAG, "setListeners: " + pharmacies.size());
            pharmavcyDetails();
        });



    }


 /*  @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = hereLocation(location.getLatitude(), location.getLongitude());
                        Log.d(TAG, "onRequestPermissionsResult: city name 2 = " + city);
                    } catch (Exception e) {
                        Log.d(TAG, "onRequestPermissionsResult: " + e.getMessage());
                    }

                } else {
                    Toast.makeText(this, "hata", Toast.LENGTH_SHORT).show();
                }
                break;

            }

        }
    }*/

    private void cityJSONList(){


        try {
            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(this.getResources().openRawResource(R.raw.city_pharmacy_details)));
            StringBuilder jsonBuilder = new StringBuilder();
            for (String line = null; (line = jsonReader.readLine()) != null;){
                jsonBuilder.append(line).append("\n");
            }

            Gson gson = new Gson();
            cityPharmacy = gson.fromJson(jsonBuilder.toString(), CityPharmacy.class);

        }catch (FileNotFoundException e){
            Log.e(TAG, "jsonFile: ",e);
        }catch (IOException e){
            Log.e(TAG, "jsonFile: ", e);
        }

    }

    private void districtSelect(){

        for (int i = 0; i < cityPharmacy.getCityDetail().size(); i++){
            spinnerData.add(cityPharmacy.getCityDetail().get(i).getName());
        }
        binding.citySpinner.setAdapter(dataAdapter);
        binding.citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position = i;
                spinnerData2.clear();
                for (int x = 0; x < cityPharmacy.getCityDetail().get(i).getIlceler().size(); x++){
                    spinnerData2.add(cityPharmacy.getCityDetail().get(i).getIlceler().get(x).getIlceAdi());
                }
                binding.districtSpinner.setAdapter(dataAdapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position2 = i;
                for (int x = 0; x < cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().size(); x++){

                    pharmacyName = cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().get(x).getEczaneAdi();
                    pharmacyAdress = cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().get(x).getEczaneAdres();
                    pharmacyPhone = cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().get(x).getEczaneTel();
                    pharmacyLatLng = cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().get(x).getEczaneLatLng();
                    spinnerData3.add(pharmacyName);
                    spinnerData3.add(pharmacyAdress);
                    spinnerData3.add(pharmacyPhone);

                    Pharmacy pharmacy = new Pharmacy(pharmacyName,pharmacyAdress,pharmacyPhone,pharmacyLatLng);

                    pharmacies.add(pharmacy);
                    Log.d(TAG, "onItemSelected: " + spinnerData3);

                    spinnerData3.add(cityPharmacy.getCityDetail().get(position).getIlceler().get(i).getEczaneler().get(x).getEczaneAdi());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void pharmavcyDetails(){
        pharmacies.clear();


        for (int i = 0; i < cityPharmacy.getCityDetail().get(position).getIlceler().get(position2).getEczaneler().size(); i++){
            Pharmacy pharmacy = new Pharmacy();

            pharmacy.pharmacyName = cityPharmacy.getCityDetail().get(position).getIlceler().get(position2).getEczaneler().get(i).getEczaneAdi();
            pharmacy.pharmacyAdress = cityPharmacy.getCityDetail().get(position).getIlceler().get(position2).getEczaneler().get(i).getEczaneAdres();
            pharmacy.pharmacyPhone = cityPharmacy.getCityDetail().get(position).getIlceler().get(position2).getEczaneler().get(i).getEczaneTel();
            pharmacy.latlng = cityPharmacy.getCityDetail().get(position).getIlceler().get(position2).getEczaneler().get(i).getEczaneLatLng();

            pharmacies.add(pharmacy);
            Log.d(TAG, "pharmavcyDetails: for " + pharmacy.latlng);

        }
        if (pharmacies.size() > 0){
            Log.d(TAG, "pharmavcyDetails: " + pharmacies);
            binding.recyclerView.setHasFixedSize(true);
            binding.recyclerView.setAdapter(pharmacyAdapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    private String hereLocation(double lat, double lng){

        String cityName = "";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(lat,lng,10);
            if (addresses.size() > 0 ){
                for (Address adr: addresses){
                    if (adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
                }
            }
        }catch (IOException e){
            Log.d(TAG, "hereLocation: " + e.getMessage());
        }
        return cityName;
    }

    public void mapsOpen(Pharmacy pharmacy){

        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + pharmacy.latlng +"&mode=w");

        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        mapIntent.setPackage("com.google.android.apps.maps");

        startActivity(mapIntent);


        }

    

    @Override
    public void onPharmacyClicked(Pharmacy pharmacy) {
        mapsOpen(pharmacy);

    }
}