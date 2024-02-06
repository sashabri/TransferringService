package com.example.transferringservice.controller.dto;

public class UnsuccessfulResponse {
    private String message;

    private Integer id;

    public UnsuccessfulResponse(String message, Integer id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public Integer getId() {
        return id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
