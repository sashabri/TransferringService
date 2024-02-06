package com.example.transferringservice.service;

import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.CardRepository;
import com.example.transferringservice.repository.OperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultTransferService implements TransferService {

    private static final Logger log = LoggerFactory.getLogger(DefaultTransferService.class);

    private final OperationRepository defaultOperation;
    private final CardRepository defaultCardRepository;
    private final DataValidator dataValidator;


    @Autowired
    public DefaultTransferService(OperationRepository defaultOperation, CardRepository defaultCardRepository, DataValidator dataValidator) {
        this.defaultOperation = defaultOperation;
        this.defaultCardRepository = defaultCardRepository;
        this.dataValidator = dataValidator;
    }

    @Override
    public String transfer(TransferRequestBody transferRequestBody) throws InvalidDataException {
        Card cardFrom = defaultCardRepository.findCard(transferRequestBody.getCardFromNumber());
        Card cardTo = defaultCardRepository.findCard(transferRequestBody.getCardToNumber());

        dataValidator.validCardData(cardFrom, cardTo, transferRequestBody);

        return defaultOperation.createOperation(
                cardFrom.getNumber(),
                cardTo.getNumber(),
                transferRequestBody.getAmount().getValue()
        );
    }

    @Override
    public String confirmOperation(ConfirmOperationRequestBody confirmOperationRequestBody) throws InternalServerErrorException, InvalidDataException {
        Operation operation = defaultOperation.find(confirmOperationRequestBody.getOperationId());

        dataValidator.validOperation(operation, confirmOperationRequestBody);

        defaultCardRepository.changeBalance(operation.getNumCardFrom(), -operation.getSum());
        defaultCardRepository.changeBalance(operation.getNumCardTo(), operation.getSum());
        defaultOperation.setSuccess(operation.getOperationId());
        log.info(
                "№ карты, с которой было списание " + operation.getNumCardFrom() +
                        " № карты зачисления " + operation.getNumCardTo() +
                        " сумма списания " + operation.getSum() +
                        " операция № " + operation.getOperationId() + " прошла успешно"
        );

        return operation.getOperationId();
    }
}
