package com.cyber.mysticism.tarot.controller.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class EmailValidatorTest {

    @InjectMocks
    private EmailValidator emailValidator;

    @Test
    void isEmailValid_success() {
        assertTrue(emailValidator.isEmailValid("email@email.com"));
    }

    @Test
    void isEmailValid_failure() {
        assertFalse(emailValidator.isEmailValid("email"));
    }
}
