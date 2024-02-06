package com.example.transferringservice.service;

import com.example.transferringservice.controller.dto.Amount;
import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.CardRepository;
import com.example.transferringservice.repository.OperationRepository;
import com.example.transferringservice.service.DataValidator;
import com.example.transferringservice.service.DefaultTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DefaultTransferServiceTest {
    CardRepository cardRepository = Mockito.mock(CardRepository.class);
    OperationRepository operationRepository = Mockito.mock(OperationRepository.class);

    DataValidator dataValidator = Mockito.mock(DataValidator.class);
    DefaultTransferService sut = new DefaultTransferService(operationRepository, cardRepository, dataValidator);

    @Test
    public void transferTest() throws InvalidDataException {

        TransferRequestBody transferRequestBody = new TransferRequestBody("7896214556985874",
                "12/23",
                "568",
                "7896214556555558",
                new Amount(300, "RU")
        );

        Card cardFrom = new Card(
                "7896214556985874",
                "12/23",
                "568",
                21500,
                "RU"
        );

        Card cardTo = new Card(
                "7896214556555558",
                "11/23",
                "214",
                300000,
                "RU"
        );

        Mockito.doReturn(cardFrom).when(cardRepository).findCard("7896214556985874");
        Mockito.doReturn(cardTo).when(cardRepository).findCard("7896214556555558");
        Mockito.doReturn("1").when(operationRepository).createOperation(
                cardFrom.getNumber(), cardTo.getNumber(), transferRequestBody.getAmount().getValue()
        );

        String actual = sut.transfer(transferRequestBody);

        Mockito.verify(dataValidator, Mockito.times(1)).validCardData(cardFrom, cardTo, transferRequestBody);

        Assertions.assertEquals("1", actual);
    }
    @Test
    public void confirmOperationTest() throws InvalidDataException, InternalServerErrorException {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        String numberCardFrom = "7896214556985874";

        Operation operation = new Operation(
                "1",
                "0000",
                numberCardFrom,
                "7896214556555558",
                500,
                false
        );

        Card cardFrom = new Card(
                numberCardFrom,
                "12/23",
                "568",
                21500,
                "RU"
        );

        Mockito.doReturn(operation).when(operationRepository).find("1");
        Mockito.doReturn(cardFrom).when(cardRepository).findCard(numberCardFrom);

        String actual = sut.confirmOperation(confirmOperationRequestBody);

        Mockito.verify(cardRepository, Mockito.times(1)).changeBalance(numberCardFrom, -operation.getSum());
        Mockito.verify(cardRepository, Mockito.times(1)).changeBalance(operation.getNumCardTo(), operation.getSum());
        Mockito.verify(operationRepository, Mockito.times(1)).setSuccess(operation.getOperationId());
        Mockito.verify(dataValidator, Mockito.times(1)).validOperation(operation, confirmOperationRequestBody);

        Assertions.assertEquals("1", actual);
    }
}
