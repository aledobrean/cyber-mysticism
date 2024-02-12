package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.repository.TarotCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TarotCardServiceTest {

    @Mock
    private TarotCardRepository tarotCardRepository;
    @InjectMocks
    private TarotCardService tarotCardService;

    @Test
    void getCards() {
        when(tarotCardRepository.findAll()).thenReturn(List.of(new TarotCard()));

        assertFalse(tarotCardService.getCards().isEmpty());
    }

    @Test
    void getMajorArcanaCards() {
        when(tarotCardRepository.findAll()).thenReturn(List.of(
                new TarotCard("The Fool", 0, "Major Arcana", "0", List.of())));

        assertFalse(tarotCardService.getMajorArcanaCards().isEmpty());
    }
}
