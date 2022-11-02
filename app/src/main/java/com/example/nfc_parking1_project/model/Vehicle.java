package com.example.nfc_parking1_project.model;

public class Vehicle {

    private String cardId;
    private String plateId;

    public Vehicle(String cardId, String plateId) {

        this.cardId = cardId;
        this.plateId = plateId;
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
