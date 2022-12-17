package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class Auth {
    @SerializedName("phonenumber")
    private String phoneNumber;
    private String password;
    @SerializedName("newpassword")
    private String newPassword;


    public Auth(String phoneNumber, String password) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Auth() {
    }

    @Override
    public String toString() {
        return "Auth{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    ;

}
