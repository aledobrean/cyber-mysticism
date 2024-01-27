package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotUser;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

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

        assertEquals(1, userService.findAll().size());
    }

    @Test
    void findByUsername() {
        when(userRepository.findById("user")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertTrue(userService.findByUsername("user").isPresent()),
                () -> assertEquals(tarotUser.getUsername(), userService.findByUsername("user").get().getUsername())
        );
    }

    @Test
    void findByUsername_userDoesNotExist() {
        when(userRepository.findById("user")).thenReturn(Optional.empty());

        assertFalse(userService.findByUsername("user").isPresent());
    }

    @Test
    void findByEmail() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertTrue(userService.findByEmail("email").isPresent()),
                () -> assertEquals(tarotUser.getEmail(), userService.findByEmail("email").get().getEmail())
        );
    }

    @Test
    void findByEmail_userDoesNotExist() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        assertFalse(userService.findByEmail("email").isPresent());
    }

    @Test
    void save() throws UserAlreadyExistsException {
        when(userRepository.findById("user")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        userService.save(tarotUser);

        verify(userRepository).save(tarotUser);
    }

    @Test
    void save_usernameExists() {
        when(userRepository.findById("user")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertThrows(UserAlreadyExistsException.class, () -> userService.save(tarotUser)),
                () -> verify(userRepository, never()).save(tarotUser)
        );
    }

    @Test
    void save_emailExists() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(tarotUser));

        assertAll(
                () -> assertThrows(UserAlreadyExistsException.class, () -> userService.save(tarotUser)),
                () -> verify(userRepository, never()).save(tarotUser)
        );
    }

    @Test
    void deleteByUsername() throws UserNotFoundException {
        when(userRepository.findById("user")).thenReturn(Optional.of(tarotUser));

        userService.deleteByUsername("user");

        verify(userRepository).deleteById("user");
    }

    @Test
    void deleteByUsername_userNotFound() {
        assertAll(
                () -> assertThrows(UserNotFoundException.class, () -> userService.deleteByUsername("user")),
                () -> verify(userRepository, never()).deleteById("user")
        );
    }
}
