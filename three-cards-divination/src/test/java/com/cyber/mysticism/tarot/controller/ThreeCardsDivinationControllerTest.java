package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class ThreeCardsDivinationControllerTest {

    @Mock
    private ThreeCardsDivinationService threeCardsDivinationService;
    @InjectMocks
    private ThreeCardsDivinationController threeCardsDivinationController;

    @Test
    void performReading() throws Exception {
        Card card = new Card("The Fool", 0, "Major Arcana", "m00.jpg", List.of("Watch for new projects and new beginnings"));
        Map<String, Card> extracted = Map.of("past", card);
        when(threeCardsDivinationService.getReading()).thenReturn(extracted);

        assertEquals(extracted, threeCardsDivinationController.performReading());
    }

    @Test
    void performReading_zeroCards() throws Exception {
        Map<String, Card> extracted = Map.of();
        when(threeCardsDivinationService.getReading()).thenReturn(extracted);

        assertEquals(extracted, threeCardsDivinationController.performReading());
    }
}
