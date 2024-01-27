package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.exceptions.ForbiddenAccessException;
import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@RestController
public class ThreeCardsDivinationController {

    public static final String UNAUTHORIZED = "Unauthorized!";
    private final ThreeCardsDivinationService threeCardsDivinationService;

    @Autowired
    public ThreeCardsDivinationController(ThreeCardsDivinationService threeCardsDivinationService) {
        this.threeCardsDivinationService = threeCardsDivinationService;
    }

    @GetMapping("/three-cards-divination")
    public Map<String, Card> performReadingForUser(@RequestHeader(value = "Authorization", required = false) String authHeader)
            throws DivinationException, ForbiddenAccessException, UnauthorisedAccessException {
        String[] credentials = authenticateAndParseHeader(authHeader);
        String username = credentials[0];
        String email = credentials[1];
        try {
            return threeCardsDivinationService.getReadingForUser(username, email);
        } catch (UserNotFoundException e) {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }

    }

    private String[] authenticateAndParseHeader(String authHeader) throws UnauthorisedAccessException, ForbiddenAccessException {
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
