package com.cyber.mysticism.tarot.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
public class DivinationException extends Exception {
    public DivinationException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DivinationException(String message) {
        super(message);
    }
}
