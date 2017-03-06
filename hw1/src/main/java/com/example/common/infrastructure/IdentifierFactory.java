package com.example.common.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdentifierFactory {
    public String nextId() {
        return UUID.randomUUID().toString();
    }
}
