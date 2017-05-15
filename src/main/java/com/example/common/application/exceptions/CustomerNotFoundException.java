package com.example.common.application.exceptions;

public class CustomerNotFoundException extends Exception {
    public CustomerNotFoundException(String msg) {
        super(msg);
    }
}
