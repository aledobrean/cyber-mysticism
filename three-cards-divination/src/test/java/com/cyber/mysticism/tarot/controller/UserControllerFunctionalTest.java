package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.util.AuthenticationUtil;
import com.cyber.mysticism.tarot.controller.util.EmailValidator;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.service.ReadingService;
import com.cyber.mysticism.tarot.service.TarotUserService;
import com.cyber.mysticism.tarot.service.exceptions.UnauthorisedAccessException;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";
    private static final TarotUser user;

    static {
        user = new TarotUser();
        user.setUsername("user");
        user.setEmail("email");
    }

    @MockBean
    private AuthenticationUtil authenticationUtil;
    @MockBean
    private EmailValidator emailValidator;
    @Mock
    private ReadingService readingService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TarotUserService tarotUserService;

    @Test
    void findAll() throws Exception {
        when(tarotUserService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk());
    }

    @Test
    void getReadingsForUser() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(readingService.getAllReadingsForUser(user)).thenReturn(List.of());

        mockMvc.perform(get("/user/reading")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk());
    }

    @Test
    void getReadingsForUser_unauthorised() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenThrow(UnauthorisedAccessException.class);

        mockMvc.perform(get("/user/reading")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_success() throws Exception {
        when(emailValidator.isEmailValid("email")).thenReturn(true);
        when(tarotUserService.save(any(TarotUser.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getTarotUserAsJson()))
                .andExpect(status().isCreated());
    }

    @Test
    void create_userAlreadyExists() throws Exception {
        when(emailValidator.isEmailValid("email")).thenReturn(true);
        when(tarotUserService.save(any(TarotUser.class))).thenThrow(UserAlreadyExistsException.class);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getTarotUserAsJson()))
                .andExpect(status().isConflict());
    }

    @Test
    void create_invalidEmail() throws Exception {
        when(tarotUserService.save(any(TarotUser.class))).thenReturn(user);

        mockMvc.perform(post("/user")
                        .contentType(MediaType.APPLICATION_JSON).content(getTarotUserAsJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_success() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        doNothing().when(tarotUserService).deleteByUsername("user");

        mockMvc.perform(delete("/user/user")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_userNotFound() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenThrow(UnauthorisedAccessException.class);
        doThrow(UserNotFoundException.class).when(tarotUserService).deleteByUsername("username");

        mockMvc.perform(delete("/user/username")
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isUnauthorized());
    }

    private String getTarotUserAsJson() throws JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(user);
    }
}
