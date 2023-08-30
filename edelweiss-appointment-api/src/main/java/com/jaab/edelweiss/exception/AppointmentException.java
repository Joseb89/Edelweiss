package com.jaab.edelweiss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AppointmentException extends RuntimeException {
    public AppointmentException(String message) { super(message); }
}
