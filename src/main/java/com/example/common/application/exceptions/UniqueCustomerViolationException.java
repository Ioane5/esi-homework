package com.example.common.application.exceptions;

public class UniqueCustomerViolationException  extends Exception {
    public UniqueCustomerViolationException(String msg) {
        super(msg);
    }
}
