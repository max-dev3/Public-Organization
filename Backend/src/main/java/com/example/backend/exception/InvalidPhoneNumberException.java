package com.example.backend.exception;

public class InvalidPhoneNumberException extends RuntimeException {
    public InvalidPhoneNumberException(String phoneNumber) {
        super("Invalid Phone Number");
    }
}