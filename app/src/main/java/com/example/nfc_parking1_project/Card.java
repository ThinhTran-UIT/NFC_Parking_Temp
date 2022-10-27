package com.example.nfc_parking1_project;

public class Card {
    private String cardId;
    private String status;

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
