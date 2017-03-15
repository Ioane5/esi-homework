package com.example.common.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PurchaseOrderNotFoundException extends Exception {
    public PurchaseOrderNotFoundException() {
        super();
    }
}
