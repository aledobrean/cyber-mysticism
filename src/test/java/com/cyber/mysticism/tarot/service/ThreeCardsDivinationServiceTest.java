package com.cyber.mysticism.tarot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ThreeCardsDivinationServiceTest {

    @Autowired
    private ThreeCardsDivinationService service;

    @Test
    void getReading() throws Exception {
        assertEquals(3, service.getReading().size());
    }

    @Test
    void getCards() {
        assertEquals(78, service.getCards().size());
    }

    @Test
    void getMajorArcanaCards() {
        assertEquals(22, service.getMajorArcanaCards().size());
    }
}
