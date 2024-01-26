package com.example.transferringservice.repository;

import com.example.transferringservice.model.Card;

public interface CardRepository {

    public Card findCard(String cardNumber);
    public void changeBalance(String cardNumber, Integer sum);
}
