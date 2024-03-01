package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.service.TarotCardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarotDeckControllerTest {

    @Mock
    private TarotCardService tarotCardService;
    @InjectMocks
    private TarotDeckController tarotDeckController;

    @Test
    void getCards() {
        when(tarotCardService.getCards()).thenReturn(List.of(new TarotCard()));

        assertFalse(tarotDeckController.getCards().isEmpty());
    }

    @Test
    void getMajorArcanaCards() {
        when(tarotCardService.getMajorArcanaCards()).thenReturn(List.of(new TarotCard()));

        assertFalse(tarotDeckController.getMajorArcanaCards().isEmpty());
    }
}
