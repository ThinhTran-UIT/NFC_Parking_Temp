package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Card {

    private String id;

    private String status;

    @SerializedName("created_by")
    private int createdBy;

    @SerializedName("updated_by")
    private int updatedBy;

    @SerializedName("created_at")
    private Date createdDate;

    @SerializedName("updated_at")
    private Date updatedDate;

    private boolean isDeleted;

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
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

    public Card(){};
    public Card(String cardId, String status) {
        this.id = cardId;
        this.status = status;
    }

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
}
