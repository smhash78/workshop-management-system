package com.java.workshop.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyAccessException extends RuntimeException {
    public MyAccessException(String message) {
        super(message);
    }
}
