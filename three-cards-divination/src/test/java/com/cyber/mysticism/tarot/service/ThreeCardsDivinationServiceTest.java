package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThreeCardsDivinationServiceTest {

    @Mock
    private ReadingRepository readingRepository;
    @Mock
    private TarotUser tarotUser;
    @Mock
    private ThreeCardsDivinationReading threeCardsDivinationReading;
    @Mock
    private TarotUserService tarotUserService;
    @Mock
    private TarotCardService tarotCardService;
    @InjectMocks
    private ThreeCardsDivinationService threeCardsDivinationService;

    private static List<TarotCard> getCards() {
        return List.of(new TarotCard("The Fool", 0, "Major Arcana", "0", List.of()),
                new TarotCard("The Magician", 1, "Major Arcana", "1", List.of()),
                new TarotCard("Seven of Cups", 7, "Minor Arcana", "7", List.of("You're being fed a line")));
    }

    @Test
    void getReadingForUser() throws Exception {
        when(tarotCardService.getCards()).thenReturn(getCards());
        when(tarotCardService.getMajorArcanaCards()).thenReturn(getCards());
        when(tarotUserService.findByUsernameAndEmail("user", "email")).thenReturn(tarotUser);
        when(tarotUser.getUsername()).thenReturn("user");
        when(tarotUser.getEmail()).thenReturn("email");
        when(readingRepository.findByUserAndUniqueReadingCode(any(TarotUser.class), anyString())).thenReturn(emptyList());

        ThreeCardsDivinationReading reading = threeCardsDivinationService.getReadingForUser(tarotUser);

        assertAll(
                () -> verify(tarotCardService).getCards(),
                () -> verify(readingRepository).findByUserAndUniqueReadingCode(eq(tarotUser), anyString()),
                () -> assertThat(reading).matches(r -> r.getPast() != null)
                        .matches(r -> r.getPresent() != null)
                        .matches(r -> r.getFuture() != null),
                () -> verify(readingRepository).save(reading)
        );
    }

    @Test
    void getReadingForUser_duplicatedReading() throws Exception {
        when(tarotUserService.findByUsernameAndEmail("user", "email")).thenReturn(tarotUser);
        when(tarotCardService.getCards()).thenReturn(getCards());
        when(tarotCardService.getMajorArcanaCards()).thenReturn(getCards());
        when(tarotUser.getUsername()).thenReturn("user");
        when(tarotUser.getEmail()).thenReturn("email");
        when(readingRepository.findByUserAndUniqueReadingCode(any(TarotUser.class), anyString()))
                .thenReturn(List.of(threeCardsDivinationReading));

        assertThrows(DivinationException.class, () -> threeCardsDivinationService.getReadingForUser(tarotUser));

        assertAll(
                () -> verify(readingRepository, atLeast(5)).findByUserAndUniqueReadingCode(any(TarotUser.class), anyString()),
                () -> verify(readingRepository, never()).save(any())
        );
    }

    @Test
    void getReadingForUser_oneMinorArcanaCardRead_throwsException() {
        TarotCard card = new TarotCard("Seven of Cups", 7, "Minor Arcana", "7", List.of(""));
        when(tarotCardService.getMajorArcanaCards()).thenReturn(List.of(card));
        when(tarotCardService.getCards()).thenReturn(List.of(card));

        assertThrows(DivinationException.class, () -> threeCardsDivinationService.getReadingForUser(tarotUser), "No Major Arcana card was returned.");
    }

    @Test
    void getReadingForUser_zeroCards_throwsException() {
        when(tarotCardService.getMajorArcanaCards()).thenReturn(List.of());

        assertThrows(DivinationException.class, () -> threeCardsDivinationService.getReadingForUser(tarotUser));
    }

    @Test
    void getReadingForUser_invalidUser() throws Exception {
        when(tarotUserService.findByUsernameAndEmail(null, null)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> threeCardsDivinationService.getReadingForUser(tarotUser));
    }
}
