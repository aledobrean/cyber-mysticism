package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.service.UserService;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void findAll() {
        when(userService.findAll()).thenReturn(List.of(setupTarotUser()));

        assertEquals(1, userController.findAll().size());
    }

    @Test
    void findAll_emptyList() {
        when(userService.findAll()).thenReturn(List.of());

        assertEquals(0, userController.findAll().size());
    }

    @Test
    void create_success() throws Exception {
        when(userService.save(any(TarotUser.class))).thenReturn(setupTarotUser());

        assertEquals(setupTarotUser().getUsername(), userController.create(setupTarotUser()).getUsername());
    }

    @Test
    void create_userExists() throws Exception {
        when(userService.save(any(TarotUser.class))).thenThrow(UserAlreadyExistsException.class);

        assertThrows(UserAlreadyExistsException.class, () -> userController.create(setupTarotUser()));
    }

    @Test
    void deleteById_success() throws Exception {
        doNothing().when(userService).deleteByUsername(anyString());

        userController.deleteById("user");

        verify(userService).deleteByUsername("user");
    }

    @Test
    void deleteById_userNotFound() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).deleteByUsername(anyString());

        assertThrows(UserNotFoundException.class, () -> userController.deleteById("user"));
    }

    private TarotUser setupTarotUser() {
        TarotUser tarotUser = new TarotUser();
        tarotUser.setUsername("user");
        tarotUser.setEmail("email");
        return tarotUser;
    }
}
