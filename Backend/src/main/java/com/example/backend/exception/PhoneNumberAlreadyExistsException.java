package com.example.backend.exception;

public class PhoneNumberAlreadyExistsException extends RuntimeException {
    public PhoneNumberAlreadyExistsException(String phoneNumber) {
        super("User with phone number " + phoneNumber + " already exists.");
    }
}