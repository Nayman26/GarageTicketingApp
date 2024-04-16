package com.example.garageTicketingApp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Custom exception class for when the garage is full
public class GarageTicketingException extends RuntimeException {
    public GarageTicketingException(String message) {
        super(message);
    }
}