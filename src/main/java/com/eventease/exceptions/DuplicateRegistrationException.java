package com.eventease.exceptions;

public class DuplicateRegistrationException extends Exception {
    public DuplicateRegistrationException(String msg) {
        super(msg);
    }
}
