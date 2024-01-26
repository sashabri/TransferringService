package com.example.transferringservice.repository;

import com.example.transferringservice.model.Card;
import com.example.transferringservice.model.Operation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultOperation implements OperationRepository{

    private List<Operation> operations = new ArrayList<>();

    private AtomicInteger counterOperationId = new AtomicInteger(1);

    public DefaultOperation() {
    }
    @Override
    public String createOperation(String numCardFrom, String numCardTo, Integer sum) {
        String operationId = String.valueOf(counterOperationId.getAndIncrement());
        Operation operation = new Operation(operationId, "0000", numCardFrom, numCardTo, sum, false);
        operations.add(operation);

        return operation.getOperationId();
    }

    @Override
    public Operation find(String operationId) {
        for (Operation operation1 : operations) {
            if (operation1.getOperationId().equals(operationId)) {
                return operation1;
            }
        }
        return null;
    }

    @Override
    public void setSuccess(String operationId) {
        Operation operation = find(operationId);

        if (operation != null) {
            operation.setSuccess(true);
        }
    }
}
