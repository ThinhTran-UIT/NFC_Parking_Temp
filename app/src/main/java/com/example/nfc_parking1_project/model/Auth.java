package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("phonenumber")
    private String phoneNumber;
    private String password;

    public Auth(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

}
