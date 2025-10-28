package com.pm.productservice.exception;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException(String message) {
        super(message);
    }
}
