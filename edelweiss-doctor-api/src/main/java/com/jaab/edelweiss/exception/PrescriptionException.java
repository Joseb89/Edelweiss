package com.jaab.edelweiss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PrescriptionException extends RuntimeException {

    public PrescriptionException(String message) {
        super(message);
    }
}
