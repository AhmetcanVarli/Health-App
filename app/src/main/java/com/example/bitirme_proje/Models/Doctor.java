package com.example.bitirme_proje.Models;

public class Doctor {
    public String doctorName,doctorSurname,date,time,clinicName,password,email;

    public Doctor() {
    }

    public Doctor(String doctorName,String clinicName,String password,String email,String doctorSurname) {
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
        this.clinicName = clinicName;
        this.password = password;
        this.email = email;

    }



    public Doctor(String doctorName, String date, String time, String clinicName) {
        this.doctorName = doctorName;
        this.date = date;
        this.time = time;
        this.clinicName = clinicName;
    }
}
