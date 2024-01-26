package com.example.transferringservice.model;

public class Card {

    private String number;

    private String expiryDate;

    private String cvv;

    private Integer balance;

    private String currency;

    public Card() {
    }

    public  Card(String number, String expiryDate, String cvv, Integer balance, String currency) {
        this.number = number;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.balance = balance;
        this.currency = currency;
    }
    public void setNumber(String number) {
        this.number = number;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumber() {
        return number;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public Integer getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }
}
