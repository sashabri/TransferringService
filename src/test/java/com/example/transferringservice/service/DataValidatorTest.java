package com.example.transferringservice.service;

import com.example.transferringservice.controller.dto.Amount;
import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;
import com.example.transferringservice.repository.CardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class DataValidatorTest {
    CardRepository cardRepository = Mockito.mock(CardRepository.class);
    private final DataValidator sut = new DataValidator(cardRepository);

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

        String expected = "Такого номера карты - " + transferRequestBody.getCardFromNumber() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(null, cardTo, transferRequestBody);
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

        String expected = "Такого номера карты - " + transferRequestBody.getCardToNumber() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, null, transferRequestBody);
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

        String expected = "Срок действия карты - " + transferRequestBody.getCardFromNumber() + " неверный.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, cardTo, transferRequestBody);
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

        String expected = "CVV карты - " + transferRequestBody.getCardFromNumber() + " неверный.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, cardTo, transferRequestBody);
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

        String expected = "Валюта карты - " + transferRequestBody.getCardFromNumber() + " не соответствует звявленной.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, cardTo, transferRequestBody);
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

        String expected = "Валюта карты - " + transferRequestBody.getCardToNumber() + " не соответствует звявленной.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, cardTo, transferRequestBody);
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

        String expected = "Недостаточно средств для осуществления перевода.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () -> {
            sut.validCardData(cardFrom, cardTo, transferRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }

    @Test
    public void invalidDataExceptionOperationNotExistTest() {
        ConfirmOperationRequestBody confirmOperationRequestBody = new ConfirmOperationRequestBody("1", "0000");

        String expected = "Такой операции " + confirmOperationRequestBody.getOperationId() + " не существует.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.validOperation(null ,confirmOperationRequestBody);
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

        String expected = "Неверный код";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.validOperation(operation, confirmOperationRequestBody);
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

        Mockito.doReturn(cardFrom).when(cardRepository).findCard(numberCardFrom);

        String expected = "Недостаточно средств для осуществления перевода.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.validOperation(operation, confirmOperationRequestBody);
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

        Mockito.doReturn(cardFrom).when(cardRepository).findCard(numberCardFrom);

        String expected = "Операция уже завершена.";

        InvalidDataException actual = Assertions.assertThrows(InvalidDataException.class, () ->
        {
            sut.validOperation(operation, confirmOperationRequestBody);
        });

        Assertions.assertEquals(expected, actual.getMessage());
    }
}
