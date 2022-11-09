package com.example.nfc_parking1_project.model;

import com.google.gson.annotations.SerializedName;

public class Card {
    @SerializedName("CardID")
    private String cardId;
    @SerializedName("CardStatus")
    private String status;
    public Card(){};
    public Card(String cardId, String status) {
        this.cardId = cardId;
        this.status = status;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
