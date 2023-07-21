package com.jaab.edelweiss.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PharmacistNotFoundException extends RuntimeException {

    public PharmacistNotFoundException(String message) { super(message); }
}
