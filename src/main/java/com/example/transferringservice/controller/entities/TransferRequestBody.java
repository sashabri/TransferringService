package com.example.transferringservice.controller.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TransferRequestBody {
    @NotNull
    private String cardFromNumber;
    @NotNull
    private String cardFromValidTile;
    @NotNull
    @Size(min = 3, max = 3)
    private String cardFromCVV;
    @NotNull
    private String cardToNumber;
    @NotNull
    private Amount amount;

    public TransferRequestBody(String cardFromNumber, String cardFromValidTile, String cardFromCVV, String cardToNumber, Amount amount) {
        this.cardFromNumber = cardFromNumber;
        this.cardFromValidTile = cardFromValidTile;
        this.cardFromCVV = cardFromCVV;
        this.cardToNumber = cardToNumber;
        this.amount = amount;
    }

    public String getCardFromNumber() {
        return cardFromNumber;
    }

    public String getCardFromValidTile() {
        return cardFromValidTile;
    }

    public String getCardFromCVV() {
        return cardFromCVV;
    }

    public String getCardToNumber() {
        return cardToNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setCardFromNumber(String cardFromNumber) {
        this.cardFromNumber = cardFromNumber;
    }

    public void setCardFromValidTile(String cardFromValidTile) {
        this.cardFromValidTile = cardFromValidTile;
    }

    public void setCardFromCVV(String cardFromCVV) {
        this.cardFromCVV = cardFromCVV;
    }

    public void setCardToNumber(String cardToNumber) {
        this.cardToNumber = cardToNumber;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }
}
