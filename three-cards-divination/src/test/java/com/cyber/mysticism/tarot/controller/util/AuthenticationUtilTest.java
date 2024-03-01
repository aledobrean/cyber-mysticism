package com.cyber.mysticism.tarot.controller.util;

import com.cyber.mysticism.tarot.controller.exceptions.ForbiddenAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationUtilTest {

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";

    @InjectMocks
    private AuthenticationUtil authenticationUtil;

    @Test
    void authenticateAndParseHeader_valid() {
        assertAll(
                () -> assertNotNull(authenticationUtil.validateAndParseHeader(AUTH_HEADER)),
                () -> assertEquals("user", authenticationUtil.validateAndParseHeader(AUTH_HEADER)[0]),
                () -> assertEquals("email", authenticationUtil.validateAndParseHeader(AUTH_HEADER)[1])
        );
    }

    @Test
    void authenticateAndParseHeader_accessForbidden() {
        assertThrows(ForbiddenAccessException.class, () -> authenticationUtil.validateAndParseHeader(null));
    }

    @Test
    void authenticateAndParseHeader_unauthorised() {
        assertThrows(UnauthorisedAccessException.class, () -> authenticationUtil.validateAndParseHeader("unauthorised"));
    }
}
