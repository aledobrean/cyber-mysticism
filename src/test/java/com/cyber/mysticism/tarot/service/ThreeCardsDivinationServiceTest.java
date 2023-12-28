package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.repository.TarotDeckRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class ThreeCardsDivinationServiceTest {

    @Autowired
    private ThreeCardsDivinationService service;
    @MockBean
    private TarotDeckRepository tarotDeckRepository;

    @Test
    void getReading() throws Exception {
        List<Card> cards = List.of(new Card("The Fool", 0, "Major Arcana", "0", List.of()),
                new Card("The Magician", 1, "Major Arcana", "1", List.of()),
                new Card("Seven of Cups", 7, "Minor Arcana", "7", List.of("You're being fed a line")));
        when(tarotDeckRepository.getCards()).thenReturn(cards);

        Map<String, Card> reading = service.getReading();

        assertThat(reading).hasSize(3)
                .matches(map -> map.get("past") != null)
                .matches(map -> map.get("present") != null)
                .matches(map -> map.get("future") != null);
    }

    @Test
    void getReading_oneMajorArcanaCardRead_throwsException() {
        Card card = new Card("The Fool", 0, "Major Arcana", "0", List.of("Watch for new projects and new beginnings"));
        when(tarotDeckRepository.getCards()).thenReturn(List.of(card));

        assertThrows(DivinationException.class, () -> service.getReading(), "Three Cards Tarot reading failed.");
    }

    @Test
    void getReading_oneMinorArcanaCardRead_throwsException() {
        Card card = new Card("Seven of Cups", 7, "Minor Arcana", "7", List.of(""));
        when(tarotDeckRepository.getCards()).thenReturn(List.of(card));

        assertThrows(DivinationException.class, () -> service.getReading(), "No Major Arcana cards was returned.");
    }

    @Test
    void getReading_zeroCards_throwsException() {
        when(tarotDeckRepository.getCards()).thenReturn(List.of());

        assertThrows(DivinationException.class, () -> service.getReading(), "No Major Arcana cards was returned.");
    }
}
