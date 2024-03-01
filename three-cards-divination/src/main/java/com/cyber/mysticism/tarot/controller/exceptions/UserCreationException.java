package com.cyber.mysticism.tarot.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UserCreationException extends Exception {
    public UserCreationException(String message) {
        super(message);
    }
}
