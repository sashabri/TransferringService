package com.example.transferringservice.repository;

import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;

public interface OperationRepository {
    public String createOperation(String numCardFrom, String numCardTo, Integer sum);

    public Operation find(String operationId);

    public void setSuccess(String operationId);
}
