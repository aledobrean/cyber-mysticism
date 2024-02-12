package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.util.AuthenticationUtil;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreeCardsDivinationControllerTest {

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";
    @Mock
    private ThreeCardsDivinationService threeCardsDivinationService;
    @Mock
    private ThreeCardsDivinationReading threeCardsDivinationReading;
    @Mock
    private AuthenticationUtil authenticationUtil;
    @InjectMocks
    private ThreeCardsDivinationController threeCardsDivinationController;

    @Test
    void performReading_authorized() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(threeCardsDivinationService.getReadingForUser(any(TarotUser.class))).thenReturn(threeCardsDivinationReading);

        assertEquals(threeCardsDivinationReading, threeCardsDivinationController.performReadingForUser(AUTH_HEADER));
    }

    @Test
    void performReading_zeroCards_authorized() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(threeCardsDivinationService.getReadingForUser(any(TarotUser.class))).thenReturn(threeCardsDivinationReading);

        assertEquals(threeCardsDivinationReading, threeCardsDivinationController.performReadingForUser(AUTH_HEADER));
    }

    @Test
    void performReading_unauthorized() throws Exception {
        when(authenticationUtil.validateAndParseHeader(anyString())).thenThrow(UnauthorisedAccessException.class);

        assertThrows(UnauthorisedAccessException.class,
                () -> threeCardsDivinationController.performReadingForUser("unauthorized"));
    }

    @Test
    void performReading_unauthorized_userNotFound() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(threeCardsDivinationService.getReadingForUser(any(TarotUser.class))).thenThrow(UserNotFoundException.class);

        assertThrows(UnauthorisedAccessException.class,
                () -> threeCardsDivinationController.performReadingForUser(AUTH_HEADER));
    }
}
