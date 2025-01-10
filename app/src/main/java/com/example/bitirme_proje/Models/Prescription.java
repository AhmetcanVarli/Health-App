package com.example.bitirme_proje.Models;

public class Prescription {
    public String userName, userId, userPrescription,doctorName,clinicName,date,time;

    public Prescription() {
    }

    public Prescription(String userName, String userId, String userPrescription, String doctorName, String clinicName, String date, String time) {
        this.userName = userName;
        this.userId = userId;
        this.userPrescription = userPrescription;
        this.doctorName = doctorName;
        this.clinicName = clinicName;
        this.date = date;
        this.time = time;
    }
}
