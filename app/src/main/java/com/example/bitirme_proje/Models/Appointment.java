package com.example.bitirme_proje.Models;

import com.example.bitirme_proje.Models.User;

public class Appointment {
    public String clinicName, doctorName, date, clock, userName, userSurname, userID;



    public Appointment() {
    }

    public Appointment(String clinicName, String doctorName, String date, String clock, String userName, String userSurname, String userID) {
        this.clinicName = clinicName;
        this.doctorName = doctorName;
        this.date = date;
        this.clock = clock;
        this.userName = userName;
        this.userSurname = userSurname;
        this.userID = userID;
    }
}
