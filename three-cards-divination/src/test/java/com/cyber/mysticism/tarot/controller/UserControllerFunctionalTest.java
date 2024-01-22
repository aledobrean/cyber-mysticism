package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.service.UserService;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerFunctionalTest {

    private static final TarotUser user;

    static {
        user = new TarotUser();
        user.setUsername("user");
        user.setEmail("email");
    }

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void findAll() throws Exception {
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    void create_success() throws Exception {
        when(userService.save(any(TarotUser.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getTarotUserAsJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void create_userAlreadyExists() throws Exception {
        when(userService.save(any(TarotUser.class))).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getTarotUserAsJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void deleteById_success() throws Exception {
        doNothing().when(userService).deleteByUsername("username");

        mockMvc.perform(delete("/user/username"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_userNotFound() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).deleteByUsername("username");

        mockMvc.perform(delete("/user/username"))
                .andExpect(status().isNotFound());
    }

    private String getTarotUserAsJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(UserControllerFunctionalTest.user);
    }
}
