package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ThreeCardsDivinationControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ThreeCardsDivinationService threeCardsDivinationService;

    @Test
    void performReading_oneCard() throws Exception {
        Card card = new Card("The Fool", 0, "Major Arcana", "m00.jpg", List.of("Watch for new projects and new beginnings"));
        Map<String, Card> extracted = Map.of("past", card);
        when(threeCardsDivinationService.getReading()).thenReturn(extracted);

        mockMvc.perform(get("/three-cards-divination"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.past.name").exists());
    }

    @Test
    void performReading_zeroCards() throws Exception {
        when(threeCardsDivinationService.getReading()).thenReturn(Map.of());

        mockMvc.perform(get("/three-cards-divination"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.past.name").doesNotExist());
    }

    @Test
    void performReading_throwsException() throws Exception {
        when(threeCardsDivinationService.getReading()).thenThrow(DivinationException.class);

        mockMvc.perform(get("/three-cards-divination"))
                .andExpect(status().isPreconditionFailed());
    }
}
