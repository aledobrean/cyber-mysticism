package com.cyber.mysticism.tarot.controller;

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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";
    @Mock
    private TarotUserService tarotUserService;
    @Mock
    private AuthenticationUtil authenticationUtil;
    @Mock
    private EmailValidator emailValidator;
    @Mock
    private ReadingService readingService;
    @Mock
    private Reading reading;
    @InjectMocks
    private UserController userController;

    @Test
    void findAll() {
        when(tarotUserService.findAll()).thenReturn(List.of(setupTarotUser()));

        assertEquals(1, userController.findAll().size());
    }

    @Test
    void findAll_emptyList() {
        when(tarotUserService.findAll()).thenReturn(List.of());

        assertEquals(0, userController.findAll().size());
    }

    @Test
    void getReadingsForUser() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(readingService.getAllReadingsForUser(any(TarotUser.class))).thenReturn(List.of(reading));

        assertEquals(List.of(reading), userController.getReadingsForUser(AUTH_HEADER));
    }

    @Test
    void getReadingsForUser_userNotFound() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(readingService.getAllReadingsForUser(any(TarotUser.class))).thenThrow(UserNotFoundException.class);

        assertThrows(UnauthorisedAccessException.class, () -> userController.getReadingsForUser(AUTH_HEADER));
    }

    @Test
    void create_success() throws Exception {
        when(tarotUserService.save(any(TarotUser.class))).thenReturn(setupTarotUser());
        when(emailValidator.isEmailValid("email")).thenReturn(true);

        assertEquals(setupTarotUser().getUsername(), userController.create(setupTarotUser()).getUsername());
    }

    @Test
    void create_userExists() throws Exception {
        when(tarotUserService.save(any(TarotUser.class))).thenThrow(UserAlreadyExistsException.class);
        when(emailValidator.isEmailValid("email")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> userController.create(setupTarotUser()));
    }

    @Test
    void create_invalidEmail() {
        when(emailValidator.isEmailValid("email")).thenReturn(false);

        assertThrows(UserCreationException.class, () -> userController.create(setupTarotUser()));
    }

    @Test
    void deleteById_success() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        doNothing().when(tarotUserService).deleteByUsername("user");

        userController.deleteById("user", AUTH_HEADER);

        verify(tarotUserService).deleteByUsername("user");
    }

    @Test
    void deleteById_userNotFound() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        doThrow(UserNotFoundException.class).when(tarotUserService).deleteByUsername("user");

        assertThrows(UnauthorisedAccessException.class, () -> userController.deleteById("user", AUTH_HEADER));
    }

    @Test
    void deleteById_userUnauthorised() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});

        assertThrows(UnauthorisedAccessException.class, () -> userController.deleteById("different_user", AUTH_HEADER));
    }

    private TarotUser setupTarotUser() {
        TarotUser tarotUser = new TarotUser();
        tarotUser.setUsername("user");
        tarotUser.setEmail("email");
        return tarotUser;
    }
}
