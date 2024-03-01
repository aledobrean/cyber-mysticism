package com.cyber.mysticism.tarot.controller.util;

import com.cyber.mysticism.tarot.controller.exceptions.ForbiddenAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthenticationUtil {
    public static final String UNAUTHORIZED = "Unauthorized!";

    /**
     * @param authHeader in format user:email in base64
     * @return array containing the user and the email
     * @throws UnauthorisedAccessException when the authorisation header is not properly
     * @throws ForbiddenAccessException    when no authorisation header was provided
     */
    public String[] validateAndParseHeader(String authHeader) throws UnauthorisedAccessException, ForbiddenAccessException {
        if (authHeader == null) {
            throw new ForbiddenAccessException("Access forbidden!");
        } else if (!authHeader.isEmpty() && authHeader.toLowerCase().startsWith("basic")) {
            String base64Credentials = authHeader.substring("Basic".length()).trim();
            return getCredentialsFromHeaders(base64Credentials);
        } else {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }
    }

    private String[] getCredentialsFromHeaders(String base64Credentials) {
        byte[] credentialsDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credentialsDecoded, StandardCharsets.UTF_8);
        return credentials.split(":", 2);
    }
}
