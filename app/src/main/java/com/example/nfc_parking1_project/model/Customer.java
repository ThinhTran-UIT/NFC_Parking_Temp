package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class Customer {
    @SerializedName("license_number")
    private String licenseNumber;
    @SerializedName("time_visit")
    private int timeVisit;
    private int point;
    @SerializedName("created_at")
    private String createdDate;
    @SerializedName("updated_at")
    private String updatedDate;


    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getTimeVisit() {
        return timeVisit;
    }

    public void setTimeVisit(int timeVisit) {
        this.timeVisit = timeVisit;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "licenseNumber='" + licenseNumber + '\'' +
                ", timeVisit=" + timeVisit +
                ", point=" + point +
                ", createdDate='" + createdDate + '\'' +
                ", updatedDate='" + updatedDate + '\'' +
                '}';
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
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
}
