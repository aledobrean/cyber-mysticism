package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreeCardsDivinationControllerTest {

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";
    @Mock
    private ThreeCardsDivinationService threeCardsDivinationService;
    @InjectMocks
    private ThreeCardsDivinationController threeCardsDivinationController;

    @Test
    void performReading_authorized() throws Exception {
        Card card = new Card("The Fool", 0, "Major Arcana", "m00.jpg", List.of("Watch for new projects and new beginnings"));
        Map<String, Card> extracted = Map.of("past", card);
        when(threeCardsDivinationService.getReadingForUser("user", "email")).thenReturn(extracted);

        assertEquals(extracted, threeCardsDivinationController.performReadingForUser(AUTH_HEADER));
    }

    @Test
    void performReading_zeroCards_authorized() throws Exception {
        Map<String, Card> extracted = Map.of();
        when(threeCardsDivinationService.getReadingForUser("user", "email")).thenReturn(extracted);

        assertEquals(extracted, threeCardsDivinationController.performReadingForUser(AUTH_HEADER));
    }

    @Test
    void performReading_unauthorized() {
        assertThrows(UnauthorisedAccessException.class,
                () -> threeCardsDivinationController.performReadingForUser("unauthorized"));
    }
}
