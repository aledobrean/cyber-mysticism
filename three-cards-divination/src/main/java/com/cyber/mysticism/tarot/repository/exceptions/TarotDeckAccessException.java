package com.cyber.mysticism.tarot.repository.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TarotDeckAccessException extends Exception {
    public TarotDeckAccessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
