package com.example.transferringservice;

import com.example.transferringservice.controller.entities.Amount;
import com.example.transferringservice.controller.entities.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.CardRepository;
import com.example.transferringservice.repository.OperationRepository;
import com.example.transferringservice.service.DefaultTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;


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

    @Test
    public void invalidDataExceptionCardFromNotExist() {
        TransferRequestBody transferRequestBody = new TransferRequestBody("7896214556985874",
                "12/23",
                "568",
                "7896214556555558",
                new Amount(300, "RU")
        );

        Card cardTo = new Card(
                "7896214556555558",
                "11/23",
                "214",
                300000,
                "RU"
        );

        Mockito.doReturn(null).when(cardRepository).findCard("7896214556985874");
        Mockito.doReturn(cardTo).when(cardRepository).findCard("7896214556555558");

        String expected = "Такого номера карты - " + transferRequestBody.getCardFromNumber() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCardToNotExist() {
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

        Mockito.doReturn(cardFrom).when(cardRepository).findCard("7896214556985874");
        Mockito.doReturn(null).when(cardRepository).findCard("7896214556555558");

        String expected = "Такого номера карты - " + transferRequestBody.getCardToNumber() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }
    @Test
    public void invalidDataExceptionValidTileIncorrectly() {

        TransferRequestBody transferRequestBody = new TransferRequestBody("7896214556985874",
                "12/24",
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

        String expected = "Срок действия карты - " + transferRequestBody.getCardFromNumber() + " неверный.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCvvIncorrectly() {

        TransferRequestBody transferRequestBody = new TransferRequestBody("7896214556985874",
                "12/23",
                "564",
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

        String expected = "CVV карты - " + transferRequestBody.getCardFromNumber() + " неверный.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCurrencyCardFromIncorrectly() {

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
                "EU"
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

        String expected = "Валюта карты - " + transferRequestBody.getCardFromNumber() + " не соответствует звявленной.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCurrencyCardToIncorrectly() {

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
                "EU"
        );

        Mockito.doReturn(cardFrom).when(cardRepository).findCard("7896214556985874");
        Mockito.doReturn(cardTo).when(cardRepository).findCard("7896214556555558");

        String expected = "Валюта карты - " + transferRequestBody.getCardToNumber() + " не соответствует звявленной.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionValueIncorrectly() {

        TransferRequestBody transferRequestBody = new TransferRequestBody("7896214556985874",
                "12/23",
                "568",
                "7896214556555558",
                new Amount(21550, "RU")
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

        String expected = "Недостаточно средств для осуществления перевода.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> { sut.transfer(transferRequestBody);});

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void confirmOperationTest() throws InvalidDataException, InternalServerErrorException {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        Operation operation = new Operation(
                "1",
                "0000",
                "7896214556985874",
                "7896214556555558",
                500,
                false
        );

        Mockito.doReturn(operation).when(operationRepository).find("1");

        cardRepository.changeBalance(operation.getNumCardFrom(), operation.getSum());
        cardRepository.changeBalance(operation.getNumCardTo(), operation.getSum());
        operationRepository.setSuccess(operation.getOperationId());

        Mockito.verify(cardRepository).changeBalance(operation.getNumCardFrom(), operation.getSum());
        Mockito.verify(cardRepository).changeBalance(operation.getNumCardTo(), operation.getSum());
        Mockito.verify(operationRepository).setSuccess(operation.getOperationId());

        Mockito.doReturn(1000).when(operation.getNumCardFrom()).)

        String actual = sut.confirmOperation(confirmOperationRequestBody);

        Assertions.assertEquals("1", actual);
    }







}
