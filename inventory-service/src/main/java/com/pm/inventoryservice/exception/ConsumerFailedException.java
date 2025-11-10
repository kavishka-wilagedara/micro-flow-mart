package com.pm.inventoryservice.exception;

public class ConsumerFailedException extends RuntimeException {
    public ConsumerFailedException(String message) {
        super(message);
    }
}
