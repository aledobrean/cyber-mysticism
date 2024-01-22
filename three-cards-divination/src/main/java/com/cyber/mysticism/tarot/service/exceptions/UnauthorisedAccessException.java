package com.cyber.mysticism.tarot.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorisedAccessException extends Exception {
    public UnauthorisedAccessException(String message) {
        super(message);
    }
}
