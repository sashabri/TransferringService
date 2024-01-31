package com.example.transferringservice.controller;

import com.example.transferringservice.controller.entities.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.entities.SuccessResponse;
import com.example.transferringservice.controller.entities.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.service.TransferService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TransferController {

    TransferService defaultTransferService;

    public TransferController(TransferService defaultTransferService) {
        this.defaultTransferService = defaultTransferService;
    }

    @PostMapping("transfer")
    public Object transfer(@RequestBody TransferRequestBody transferRequestBody) throws InvalidDataException {
        return new SuccessResponse(defaultTransferService.transfer(transferRequestBody));
    }

    @PostMapping("confirmOperation")
    public Object confirmOperation(@RequestBody ConfirmOperationRequestBody confirmOperationRequestBody) throws InternalServerErrorException, InvalidDataException {
        return new SuccessResponse(defaultTransferService.confirmOperation(confirmOperationRequestBody));
    }
}
