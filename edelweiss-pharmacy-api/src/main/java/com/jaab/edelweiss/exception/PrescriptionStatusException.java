package com.jaab.edelweiss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class PrescriptionStatusException extends RuntimeException {

    public PrescriptionStatusException(String message) {
        super(message);
    }
}
