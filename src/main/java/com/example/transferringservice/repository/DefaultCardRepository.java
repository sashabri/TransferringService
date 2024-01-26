package com.example.transferringservice.repository;

import com.example.transferringservice.model.Card;

import java.util.ArrayList;
import java.util.List;

public class DefaultCardRepository implements CardRepository {

    private List<Card> cards = new ArrayList<>();

    public DefaultCardRepository() {
        cards.add(new Card("7896214556985874", "12/23", "568", 21500, "RU"));
        cards.add(new Card("7896214556555558", "11/23", "214", 300000, "RU"));
        cards.add(new Card("7896635136981369", "08/23", "658", 400, "EU"));
        cards.add(new Card("7892542587258752", "05/23", "357", 1000, "EU"));
    }

    public DefaultCardRepository(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public Card findCard(String cardNumber) {
        for (Card card1 : cards) {
            if (card1.getNumber().equals(cardNumber)) {
               return card1;
            }
        }
        return null;
    }

    @Override
    public void changeBalance(String cardNumber, Integer sum) {
        Card card = findCard(cardNumber);

        if(card != null) {
            card.setBalance(card.getBalance() + sum);
        }
    }
}
