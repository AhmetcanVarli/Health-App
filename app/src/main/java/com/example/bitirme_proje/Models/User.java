package com.example.bitirme_proje.Models;

public class User {
    public String id, name, surname,phoneNo,email,password;
    public boolean gender;

    public User() {
    }

    public User(String id, String name, String surname, String phoneNo, String email, String password, boolean gender) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNo = phoneNo;
        this.email = email;
        this.password = password;
        this.gender = gender;
    }
}
