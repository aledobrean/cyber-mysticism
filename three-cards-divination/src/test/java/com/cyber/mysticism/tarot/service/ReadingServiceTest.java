package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.Reading;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReadingServiceTest {

    @Mock
    private ReadingRepository readingRepository;
    @Mock
    private TarotUserService tarotUserService;
    @Mock
    private Reading reading;
    @InjectMocks
    private ReadingService readingService;

    @Test
    void getAllReadingsForUser() throws Exception {
        TarotUser tarotUser = new TarotUser("user", "email");
        when(tarotUserService.findByUsernameAndEmail("user", "email")).thenReturn(tarotUser);
        when(readingRepository.findByUser(tarotUser)).thenReturn(List.of(reading));

        assertEquals(List.of(reading), readingService.getAllReadingsForUser(tarotUser));
    }

    @Test
    void getAllReadingsForUser_userNotFound() throws Exception {
        when(tarotUserService.findByUsernameAndEmail("user", "email")).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> readingService.getAllReadingsForUser(new TarotUser("user", "email")));
    }
}
