package com.example.transferringservice.service;

import com.example.transferringservice.controller.entities.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.DefaultCardRepository;
import com.example.transferringservice.repository.DefaultOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultTransferService implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(DefaultTransferService.class);

    DefaultOperation defaultOperation = new DefaultOperation();
    DefaultCardRepository defaultCardRepository = new DefaultCardRepository();

    public DefaultTransferService() {
    }

    private void validCardData(Card cardFrom, Card cardTo, TransferRequestBody transferRequestBody) throws InvalidDataException {

        if (cardFrom == null) {
            throw new InvalidDataException("Такого номера карты - " + transferRequestBody.getCardFromNumber() + " не существует.");
        }

        if (cardTo == null) {
            throw new InvalidDataException("Такого номера карты - " + transferRequestBody.getCardToNumber() + " не существует.");
        }

        if (!cardFrom.getExpiryDate().equals(transferRequestBody.getCardFromValidTile())) {
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

    private String validOperation(ConfirmOperationRequestBody confirmOperationRequestBody) {
        Operation operation = defaultOperation.find(confirmOperationRequestBody.getOperationId());

        if (
                operation.getCode().equals(confirmOperationRequestBody.getCode()) &&
                        defaultCardRepository.findCard(operation.getNumCardTo()).getBalance() >= operation.getSum()
        ) {
            return operation.getOperationId();
        }

        return null;
    }

    @Override
    public String transfer(TransferRequestBody transferRequestBody) throws InvalidDataException {
        log.warn("lol");
        Card cardFrom = defaultCardRepository.findCard(transferRequestBody.getCardFromNumber());
        Card cardTo = defaultCardRepository.findCard(transferRequestBody.getCardToNumber());

        validCardData(cardFrom, cardTo, transferRequestBody);

        return defaultOperation.createOperation(
                cardFrom.getNumber(),
                cardTo.getNumber(),
                transferRequestBody.getAmount().getValue()
        );
    }

    @Override
    public String confirmOperation(ConfirmOperationRequestBody confirmOperationRequestBody) throws InternalServerErrorException {
        Operation operation = defaultOperation.find(validOperation(confirmOperationRequestBody));
        if (operation != null) {
            defaultCardRepository.changeBalance(operation.getNumCardFrom(), -operation.getSum());
            defaultCardRepository.changeBalance(operation.getNumCardTo(), operation.getSum());
            defaultOperation.setSuccess(operation.getOperationId());
            log.warn(
                    "№ карты, с которой было списание " + operation.getNumCardFrom() +
                            " № карты зачисления " + operation.getNumCardTo() +
                            " сумма списания " + operation.getSum() +
                            " операция № " + operation.getOperationId() + " прошла успешно"
            );
            return operation.getOperationId();
        } else {
            throw new InternalServerErrorException("Операция невозможна.");
        }

    }
}
