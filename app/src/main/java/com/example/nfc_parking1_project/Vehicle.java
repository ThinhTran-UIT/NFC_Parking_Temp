package com.example.nfc_parking1_project;

public class Vehicle {
    private int resourceId;
    private String cardId;
    private String plateId;

    public Vehicle(int resourceId, String cardId, String plateId) {
        this.resourceId = resourceId;
        this.cardId = cardId;
        this.plateId = plateId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }
}
