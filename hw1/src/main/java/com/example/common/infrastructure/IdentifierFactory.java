package com.example.common.infrastructure;

import java.util.UUID;

public class IdentifierFactory {
    public static String nextId() {
        return UUID.randomUUID().toString();
    }
}
