package com.example.transferringservice.service;

import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataValidator {
    private final CardRepository defaultCardRepository;

    @Autowired
    public DataValidator(CardRepository defaultCardRepository) {
        this.defaultCardRepository = defaultCardRepository;
    }

    void validCardData(Card cardFrom, Card cardTo, TransferRequestBody transferRequestBody) throws InvalidDataException {

        if (cardFrom == null) {
            throw new InvalidDataException("Такого номера карты - " + transferRequestBody.getCardFromNumber() + " не существует.");
        }

        if (cardTo == null) {
            throw new InvalidDataException("Такого номера карты - " + transferRequestBody.getCardToNumber() + " не существует.");
        }

        if (!cardFrom.getExpiryDate().equals(transferRequestBody.getCardFromValidTill())) {
            throw new InvalidDataException("Срок действия карты - " + transferRequestBody.getCardFromNumber() + " неверный.");
        }

        if (!cardFrom.getCvv().equals(transferRequestBody.getCardFromCVV())) {
            throw new InvalidDataException("CVV карты - " + transferRequestBody.getCardFromNumber() + " неверный.");
        }

        if (!cardFrom.getCurrency().equals(transferRequestBody.getAmount().getCurrency())) {
            throw new InvalidDataException("Валюта карты - " + transferRequestBody.getCardFromNumber() + " не соответствует звявленной.");
        }

        if (!cardTo.getCurrency().equals(transferRequestBody.getAmount().getCurrency())) {
            throw new InvalidDataException("Валюта карты - " + transferRequestBody.getCardToNumber() + " не соответствует звявленной.");
        }

        if (!(cardFrom.getBalance() >= transferRequestBody.getAmount().getValue())) {
            throw new InvalidDataException("Недостаточно средств для осуществления перевода.");
        }
    }

     void validOperation(Operation operation, ConfirmOperationRequestBody confirmOperationRequestBody) throws InvalidDataException{

        if (operation == null) {
            throw new InvalidDataException("Такой операции " + confirmOperationRequestBody.getOperationId() + " не существует.");
        }

        if (!operation.getCode().equals(confirmOperationRequestBody.getCode())) {
            throw new  InvalidDataException("Неверный код");
        }

        if (defaultCardRepository.findCard(operation.getNumCardFrom()).getBalance() < operation.getSum()) {
            throw new InvalidDataException("Недостаточно средств для осуществления перевода.");
        }

        if (operation.isSuccess()) {
            throw new InvalidDataException("Операция уже завершена.");
        }
    }
}
