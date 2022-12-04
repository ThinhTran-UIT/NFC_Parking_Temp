package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class History {
    private int id;

    @SerializedName("license_number")
    private String licenseNumber;

    @SerializedName("card_id")
    private String cardId;

    @SerializedName("time_in")
    private Date timeIn;

    @SerializedName("time_out")
    private Date timeOut;

    @SerializedName("userid_in")
    private int userIdIn;

    @SerializedName("userid_out")
    private int userIdOut;

    @SerializedName("created_at")
    private int createdDate;

    @SerializedName("updated_at")
    private int updatedDate;

    public History(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Date getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Date timeIn) {
        this.timeIn = timeIn;
    }

    public Date getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Date timeOut) {
        this.timeOut = timeOut;
    }

    public int getUserIdIn() {
        return userIdIn;
    }

    public void setUserIdIn(int userIdIn) {
        this.userIdIn = userIdIn;
    }

    public int getUserIdOut() {
        return userIdOut;
    }

    public void setUserIdOut(int userIdOut) {
        this.userIdOut = userIdOut;
    }

    public int getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(int createdDate) {
        this.createdDate = createdDate;
    }

    public int getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(int updatedDate) {
        this.updatedDate = updatedDate;
    }
}
