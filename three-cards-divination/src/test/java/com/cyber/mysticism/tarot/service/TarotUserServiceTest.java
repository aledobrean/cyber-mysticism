package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.repository.UserRepository;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarotUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadingRepository readingRepository;
    @InjectMocks
    private TarotUserService tarotUserService;

    private TarotUser tarotUser;

    @BeforeEach
    void setup() {
        reset(userRepository);

        tarotUser = new TarotUser();
        tarotUser.setUsername("user");
        tarotUser.setEmail("email");
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(List.of(tarotUser));

        assertEquals(1, tarotUserService.findAll().size());
    }

    @Test
    void findByUsername() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertTrue(tarotUserService.findByUsername("user").isPresent()),
                () -> assertEquals(tarotUser.getUsername(), tarotUserService.findByUsername("user").get().getUsername())
        );
    }

    @Test
    void findByUsername_userDoesNotExist() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertFalse(tarotUserService.findByUsername("user").isPresent());
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertTrue(tarotUserService.findByEmail("email").isPresent()),
                () -> assertEquals(tarotUser.getEmail(), tarotUserService.findByEmail("email").get().getEmail())
        );
    }

    @Test
    void findByEmail_userDoesNotExist() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        assertFalse(tarotUserService.findByEmail("email").isPresent());
    }

    @Test
    void findByUsernameAndEmail() throws Exception {
        when(userRepository.findByUsernameAndEmail("user", "email")).thenReturn(Optional.ofNullable(tarotUser));

        assertNotNull(tarotUserService.findByUsernameAndEmail("user", "email"));
    }

    @Test
    void findByUsernameAndEmail_userNotFound() {
        when(userRepository.findByUsernameAndEmail("user", "email")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> tarotUserService.findByUsernameAndEmail("user", "email"));
    }

    @Test
    void save() throws UserAlreadyExistsException {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        tarotUserService.save(tarotUser);

        verify(userRepository).save(tarotUser);
    }

    @Test
    void save_usernameExists() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertThrows(UserAlreadyExistsException.class, () -> tarotUserService.save(tarotUser)),
                () -> verify(userRepository, never()).save(tarotUser)
        );
    }

    @Test
    void save_emailExists() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertThrows(UserAlreadyExistsException.class, () -> tarotUserService.save(tarotUser)),
                () -> verify(userRepository, never()).save(tarotUser)
        );
    }

    @Test
    void deleteByUsername() throws UserNotFoundException {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(tarotUser));
        doNothing().when(readingRepository).deleteByUser(tarotUser);

        tarotUserService.deleteByUsername("user");

        assertAll(
                () -> verify(userRepository).deleteByUsername("user"),
                () -> verify(readingRepository).deleteByUser(tarotUser)
        );
    }

    @Test
    void deleteByUsername_userNotFound() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class, () -> tarotUserService.deleteByUsername("user")),
                () -> verify(userRepository, never()).deleteByUsername("user")
        );
    }
}
