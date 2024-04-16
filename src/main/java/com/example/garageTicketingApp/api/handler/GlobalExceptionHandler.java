package com.example.garageTicketingApp.api.handler;

import com.example.garageTicketingApp.exception.GarageTicketingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.HttpStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GarageTicketingException.class)
    @ResponseBody
    public ResponseEntity<String> handleGarageFullException(GarageTicketingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
