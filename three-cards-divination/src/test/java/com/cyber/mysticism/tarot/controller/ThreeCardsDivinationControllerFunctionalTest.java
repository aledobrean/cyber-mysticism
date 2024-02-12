package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.controller.util.AuthenticationUtil;
import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThreeCardsDivinationControllerFunctionalTest {

    public static final String AUTH_HEADER = "Basic dXNlcjplbWFpbA==";
    public static final String THREE_CARDS_DIVINATION_READING_ENDPOINT = "/three-cards-divination/reading";
    private TarotUser tarotUser;
    private ThreeCardsDivinationReading threeCardsDivinationReading;
    @Mock
    private AuthenticationUtil authenticationUtil;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ThreeCardsDivinationService threeCardsDivinationService;

    @BeforeEach
    public void setup() {
        tarotUser = new TarotUser("user", "email");
        TarotCard tarotCard = new TarotCard("The Fool", 0, "Major Arcana", "m00.jpg", List.of("Watch for new projects and new beginnings"));
        threeCardsDivinationReading = new ThreeCardsDivinationReading(tarotCard, tarotCard, tarotCard);
    }

    @Test
    void performReading_oneCard() throws Exception {
        when(authenticationUtil.validateAndParseHeader(AUTH_HEADER)).thenReturn(new String[]{"user", "email"});
        when(threeCardsDivinationService.getReadingForUser(tarotUser)).thenReturn(threeCardsDivinationReading);

        mockMvc.perform(get(THREE_CARDS_DIVINATION_READING_ENDPOINT)
                        .header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk());
    }

    @Test
    void performReading_emptyAuthHeader() throws Exception {
        mockMvc.perform(get(THREE_CARDS_DIVINATION_READING_ENDPOINT)
                        .header("Authorization", " "))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performReading_unauthorized() throws Exception {
        when(threeCardsDivinationService.getReadingForUser(tarotUser)).thenThrow(UserNotFoundException.class);

        mockMvc.perform(get(THREE_CARDS_DIVINATION_READING_ENDPOINT)
                        .header("Authorization", "unauthorized"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void performReading_noAuthHeaders() throws Exception {
        mockMvc.perform(get(THREE_CARDS_DIVINATION_READING_ENDPOINT))
                .andExpect(status().isForbidden());
    }
}
