package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class History {
    private int id;

    @SerializedName("license_number")
    private String licenseNumber;

    @SerializedName("card_id")
    private String cardId;

    @SerializedName("time_in")
    private String timeIn;

    @SerializedName("time_out")
    private String timeOut;

    @SerializedName("name_user_checkin")
    private String userCheckin;

    @SerializedName("name_user_checkout")
    private String userCheckout;

    @SerializedName("created_at")
    private String createdDate;

    @SerializedName("updated_at")
    private String updatedDate;

    @SerializedName("card_status")
    private String cardStatus;
    @SerializedName("is_lost_card")
    private int lostCardStatus;
    @SerializedName("report_lost_at")
    private String reportLostTime;
    public int getLostCardStatus() {
        return lostCardStatus;
    }

    public void setLostCardStatus(int lostCardStatus) {
        this.lostCardStatus = lostCardStatus;
    }

    public String getCardStatus() {
        return cardStatus;
    }


    public void setCardStatus(String cardStatus) {
        this.cardStatus = cardStatus;
    }

    public History() {
    }

    public History(String licenseNumber, String cardId) {
        this.licenseNumber = licenseNumber;
        this.cardId = cardId;
    }

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

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public String getUserCheckin() {
        return userCheckin;
    }

    public void setUserCheckin(String userCheckin) {
        this.userCheckin = userCheckin;
    }

    public String getUserCheckout() {
        return userCheckout;
    }

    public void setUserCheckout(String userCheckout) {
        this.userCheckout = userCheckout;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", cardId='" + cardId + '\'' +
                ", timeIn='" + timeIn + '\'' +
                ", timeOut='" + timeOut + '\'' +
                ", userCheckin='" + userCheckin + '\'' +
                ", userCheckout='" + userCheckout + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }

    ;


}
