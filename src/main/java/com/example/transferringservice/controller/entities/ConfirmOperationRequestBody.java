package com.example.transferringservice.controller.entities;

public class ConfirmOperationRequestBody {
    private String operationId;

    private String code;

    public ConfirmOperationRequestBody(String operationId, String code) {
        this.operationId = operationId;
        this.code = code;
    }

    public String getOperationId() {
        return operationId;
    }

    public String getCode() {
        return code;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
