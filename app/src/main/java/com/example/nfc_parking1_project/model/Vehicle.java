package com.example.nfc_parking1_project.model;

public class Vehicle {

    private String plateId;

    public Vehicle(String cardId, String plateId) {

        this.plateId = plateId;
    }

    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }
}
