package com.example.transferringservice;

import com.example.transferringservice.controller.entities.Amount;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.repository.CardRepository;
import com.example.transferringservice.repository.OperationRepository;
import com.example.transferringservice.service.DefaultTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class DefaultTransferServiceTest {

    CardRepository cardRepository = Mockito.mock(CardRepository.class);


    OperationRepository operationRepository = Mockito.mock(OperationRepository.class);


    DefaultTransferService sut = new DefaultTransferService(operationRepository, cardRepository);

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

        Assertions.assertEquals("1", actual);
    }
}
