package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.repository.TarotDeckRepository;
import com.cyber.mysticism.tarot.repository.UserRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThreeCardsDivinationServiceTest {

    @Mock
    private TarotDeckRepository tarotDeckRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TarotUser tarotUser;
    @InjectMocks
    private ThreeCardsDivinationService service;

    @BeforeEach
    void setup() {
        when(userRepository.findById("user")).thenReturn(Optional.of(tarotUser));
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(tarotUser));
    }

    @Test
    void getReadingForUser() throws Exception {
        List<Card> cards = List.of(new Card("The Fool", 0, "Major Arcana", "0", List.of()),
                new Card("The Magician", 1, "Major Arcana", "1", List.of()),
                new Card("Seven of Cups", 7, "Minor Arcana", "7", List.of("You're being fed a line")));
        when(tarotDeckRepository.getCards()).thenReturn(cards);

        Map<String, Card> reading = service.getReadingForUser("user", "email");

        assertAll(
                () -> assertThat(reading).hasSize(3)
                        .matches(map -> map.get("past") != null)
                        .matches(map -> map.get("present") != null)
                        .matches(map -> map.get("future") != null),
                () -> verify(tarotDeckRepository, times(2)).getCards(),
                () -> verify(userRepository).save(any(TarotUser.class))
        );
    }

    @Test
    void getReadingForUser_oneMajorArcanaCardRead_throwsException() {
        Card card = new Card("The Fool", 0, "Major Arcana", "0", List.of("Watch for new projects and new beginnings"));
        when(tarotDeckRepository.getCards()).thenReturn(List.of(card));

        assertThrows(DivinationException.class, () -> service.getReadingForUser("user", "email"), "Three Cards Tarot reading failed.");
    }

    @Test
    void getReadingForUser_oneMinorArcanaCardRead_throwsException() {
        Card card = new Card("Seven of Cups", 7, "Minor Arcana", "7", List.of(""));
        when(tarotDeckRepository.getCards()).thenReturn(List.of(card));

        assertThrows(DivinationException.class, () -> service.getReadingForUser("user", "email"), "No Major Arcana cards was returned.");
    }

    @Test
    void getReadingForUser_zeroCards_throwsException() {
        when(tarotDeckRepository.getCards()).thenReturn(List.of());

        assertThrows(DivinationException.class, () -> service.getReadingForUser("user", "email"), "No Major Arcana cards was returned.");
    }

    @Test
    void getReadingForUser_invalidUser() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(new TarotUser()));

        assertThrows(UserNotFoundException.class, () -> service.getReadingForUser("user", "email"));
    }
}
