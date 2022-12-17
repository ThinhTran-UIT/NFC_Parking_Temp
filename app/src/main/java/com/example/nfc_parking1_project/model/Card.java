package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Card {
    private String id;
    private String status = "NEW_CARD";
    @SerializedName("created_at")
    private Date createdDate;

//    @SerializedName("created_by")
//    private int createdBy;
//
//    @SerializedName("updated_by")
//    private int updatedBy;
    @SerializedName("updated_at")
    private Date updatedDate;
    private boolean isDeleted;

    public Card(String id, String status, Date createdDate, Date updatedDate, boolean isDeleted) {
        this.id = id;
        this.status = status;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.isDeleted = isDeleted;
    }

    //
//    public int getCreatedBy() {
//        return createdBy;
//    }
//
//    public void setCreatedBy(int createdBy) {
//        this.createdBy = createdBy;
//    }
//
//    public int getUpdatedBy() {
//        return updatedBy;
//    }
//
//    public void setUpdatedBy(int updatedBy) {
//        this.updatedBy = updatedBy;
//    }
    public Card() {
    }

    ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
