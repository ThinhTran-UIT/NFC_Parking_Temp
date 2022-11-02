package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class Card {
    @SerializedName("CardID")
    private int cardId;
    @SerializedName("CardStatus")
    private String status;

    public Card(int cardId, String status) {
        this.cardId = cardId;
        this.status = status;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
