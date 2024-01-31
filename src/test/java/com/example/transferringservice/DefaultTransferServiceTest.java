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
    public void invalidDataExceptionCardFromNotExistTest() {
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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCardToNotExistTest() {
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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionValidTileIncorrectlyTest() {

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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCvvIncorrectlyTest() {

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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCurrencyCardFromIncorrectlyTest() {

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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCurrencyCardToIncorrectlyTest() {

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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionValueIncorrectlyTest() {

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

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.transfer(transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
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

        Assertions.assertEquals("1", actual);
    }

    @Test
    public void invalidDataExceptionOperationNotExistTest() {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        Mockito.doReturn(null).when(operationRepository).find("1");

        String expected = "Такой операции " + confirmOperationRequestBody.getOperationId() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.confirmOperation(confirmOperationRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionCodeIncorrectlyTest() {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0001");

        String numberCardFrom = "7896214556985874";

        Operation operation = new Operation(
                "1",
                "0000",
                numberCardFrom,
                "7896214556555558",
                500,
                false
        );

        Mockito.doReturn(operation).when(operationRepository).find("1");

        String expected = "Неверный код";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.confirmOperation(confirmOperationRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionSumIncorrectlyTest() {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        String numberCardFrom = "7896214556985874";

        Operation operation = new Operation(
                "1",
                "0000",
                numberCardFrom,
                "7896214556555558",
                21600,
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

        String expected = "Недостаточно средств для осуществления перевода.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.confirmOperation(confirmOperationRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionSuccessIncorrectlyTest() {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        String numberCardFrom = "7896214556985874";

        Operation operation = new Operation(
                "1",
                "0000",
                numberCardFrom,
                "7896214556555558",
                500,
                true
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

        String expected = "Операция уже завершена.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.confirmOperation(confirmOperationRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }
}
