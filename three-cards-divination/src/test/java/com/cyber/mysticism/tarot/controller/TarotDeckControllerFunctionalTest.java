package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.service.TarotCardService;
import org.junit.jupiter.api.Test;
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
class TarotDeckControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TarotCardService tarotCardService;

    @Test
    void getCards() throws Exception {
        when(tarotCardService.getCards()).thenReturn(List.of(new TarotCard()));

        mockMvc.perform(get("/cards"))
                .andExpect(status().isOk());
    }

    @Test
    void getMajorArcanaCards() throws Exception {
        when(tarotCardService.getMajorArcanaCards()).thenReturn(List.of(new TarotCard()));

        mockMvc.perform(get("/cards/major-arcana"))
                .andExpect(status().isOk());
    }
}
