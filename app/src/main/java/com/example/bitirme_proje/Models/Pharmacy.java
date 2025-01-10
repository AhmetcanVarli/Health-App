package com.example.bitirme_proje.Models;

public class Pharmacy {

    public String pharmacyName, pharmacyAdress,pharmacyPhone,latlng;

    public Pharmacy() {
    }

    public Pharmacy(String pharmacyName, String pharmacyAdress, String pharmacyPhone, String latlng) {
        this.pharmacyName = pharmacyName;
        this.pharmacyAdress = pharmacyAdress;
        this.pharmacyPhone = pharmacyPhone;
        this.latlng = latlng;
    }
}
