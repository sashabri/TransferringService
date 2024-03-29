package com.example.transferringservice.service;

import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;

public interface TransferService {
    public String transfer(TransferRequestBody transferRequestBody) throws InvalidDataException;

    public String confirmOperation(ConfirmOperationRequestBody confirmOperationRequestBody) throws InternalServerErrorException, InvalidDataException;
}
