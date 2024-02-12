package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.exceptions.ForbiddenAccessException;
import com.cyber.mysticism.tarot.controller.util.AuthenticationUtil;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThreeCardsDivinationController {

    public static final String UNAUTHORIZED = "Unauthorized!";
    private final ThreeCardsDivinationService threeCardsDivinationService;
    private final AuthenticationUtil authenticationUtil;

    @Autowired
    public ThreeCardsDivinationController(ThreeCardsDivinationService threeCardsDivinationService,
                                          AuthenticationUtil authenticationUtil) {
        this.threeCardsDivinationService = threeCardsDivinationService;
        this.authenticationUtil = authenticationUtil;
    }

    @Operation(summary = "Perform a reading for an user", description = "Perform a three cards divination reading for an authenticated user using basic auth")
    @ApiResponse(responseCode = "200", description = "Reading performed successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ThreeCardsDivinationReading.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content())
    @ApiResponse(responseCode = "412", description = "Reading preconditions failed", content = @Content())
    @GetMapping("/three-cards-divination/reading")
    public ThreeCardsDivinationReading performReadingForUser(@Parameter(description = "Basic Auth header")
                                                             @RequestHeader(value = "Authorization", required = false) String authHeader)
            throws DivinationException, ForbiddenAccessException, UnauthorisedAccessException {
        String[] credentials = authenticationUtil.validateAndParseHeader(authHeader);
        String username = credentials[0];
        String email = credentials[1];
        TarotUser tarotUser = new TarotUser(username, email);
        try {
            return threeCardsDivinationService.getReadingForUser(tarotUser);
        } catch (UserNotFoundException e) {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }
    }
}
