package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.exceptions.ForbiddenAccessException;
import com.cyber.mysticism.tarot.controller.exceptions.UserCreationException;
import com.cyber.mysticism.tarot.controller.util.AuthenticationUtil;
import com.cyber.mysticism.tarot.controller.util.EmailValidator;
import com.cyber.mysticism.tarot.model.Reading;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.service.ReadingService;
import com.cyber.mysticism.tarot.service.TarotUserService;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    public static final String UNAUTHORIZED = "Unauthorized!";
    private final TarotUserService tarotUserService;
    private final ReadingService readingService;
    private final AuthenticationUtil authenticationUtil;
    private final EmailValidator emailValidator;

    @Autowired
    public UserController(TarotUserService tarotUserService, ReadingService readingService, AuthenticationUtil authenticationUtil, EmailValidator emailValidator) {
        this.tarotUserService = tarotUserService;
        this.readingService = readingService;
        this.authenticationUtil = authenticationUtil;
        this.emailValidator = emailValidator;
    }

    @Operation(summary = "Get all users", description = "Get a list of all users")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarotUser.class)))
    @GetMapping
    public List<TarotUser> findAll() {
        return tarotUserService.findAll();
    }

    @Operation(summary = "Get all readings for a user", description = "Retrieve all readings for an authenticated user using basic auth")
    @ApiResponse(responseCode = "200", description = "Retrieved readings successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Reading.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content())
    @GetMapping("/reading")
    public List<Reading> getReadingsForUser(@Parameter(description = "Basic Auth header")
                                            @RequestHeader(value = "Authorization", required = false) String authHeader)
            throws ForbiddenAccessException, UnauthorisedAccessException {
        String[] credentials = authenticationUtil.validateAndParseHeader(authHeader);
        String username = credentials[0];
        String email = credentials[1];
        TarotUser tarotUser = new TarotUser(username, email);
        try {
            return readingService.getAllReadingsForUser(tarotUser);
        } catch (UserNotFoundException e) {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }
    }

    @Operation(summary = "Create an user", description = "Create an user")
    @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarotUser.class)))
    @ApiResponse(responseCode = "400", description = "Email validation error", content = @Content())
    @ApiResponse(responseCode = "409", description = "User already exists", content = @Content())
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TarotUser create(@RequestBody TarotUser tarotUser) throws UserAlreadyExistsException, UserCreationException {
        if (tarotUser.getEmail() != null && emailValidator.isEmailValid(tarotUser.getEmail())) {
            return tarotUserService.save(tarotUser);
        }
        throw new UserCreationException("Failed to create user=" + tarotUser);
    }

    @Operation(summary = "Delete an user", description = "Delete an user using basic auth")
    @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content())
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content())
    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content())
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{username}")
    public void deleteById(@PathVariable String username,
                           @Parameter(description = "Basic Auth header")
                           @RequestHeader(value = "Authorization", required = false) String authHeader)
            throws UnauthorisedAccessException, ForbiddenAccessException {
        String[] credentials = authenticationUtil.validateAndParseHeader(authHeader);
        if (!username.equals(credentials[0])) {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }
        try {
            tarotUserService.deleteByUsername(username);
        } catch (UserNotFoundException e) {
            throw new UnauthorisedAccessException(UNAUTHORIZED);
        }
    }
}
