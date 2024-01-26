package com.example.transferringservice.advice;

import com.example.transferringservice.controller.entities.UnsuccessfulResponse;
import com.example.transferringservice.exception.InternalServerErrorException;
import com.example.transferringservice.exception.InvalidDataException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<UnsuccessfulResponse> invalidHandler(InvalidDataException e) {
        return new ResponseEntity<>(
                new UnsuccessfulResponse(e.toString(), HttpStatus.BAD_REQUEST.value()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<UnsuccessfulResponse> internalHandler(InternalServerErrorException e) {
        return new ResponseEntity<>(
                new UnsuccessfulResponse(
                        e.toString(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
