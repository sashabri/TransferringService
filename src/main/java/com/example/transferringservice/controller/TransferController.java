package com.example.transferringservice.controller;

import com.example.transferringservice.controller.dto.ConfirmOperationRequestBody;
import com.example.transferringservice.controller.dto.SuccessResponse;
import com.example.transferringservice.controller.dto.TransferRequestBody;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import com.example.transferringservice.service.TransferService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class TransferController {

    private final TransferService defaultTransferService;

    public TransferController(TransferService defaultTransferService) {
        this.defaultTransferService = defaultTransferService;
    }

    @PostMapping("transfer")
    public SuccessResponse transfer(@RequestBody TransferRequestBody transferRequestBody) throws InvalidDataException {
        return new SuccessResponse(defaultTransferService.transfer(transferRequestBody));
    }

    @PostMapping("confirmOperation")
    public SuccessResponse confirmOperation(@RequestBody ConfirmOperationRequestBody confirmOperationRequestBody) throws InternalServerErrorException, InvalidDataException {
        return new SuccessResponse(defaultTransferService.confirmOperation(confirmOperationRequestBody));
    }
}
