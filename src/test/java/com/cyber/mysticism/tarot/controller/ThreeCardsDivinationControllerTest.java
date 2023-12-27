package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.json.Card;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ThreeCardsDivinationControllerTest {

    @Autowired
    ThreeCardsDivinationController threeCardsDivinationController;

    @Test
    void performReading() {
        Map<String, Card> reading = threeCardsDivinationController.performReading();
        assertEquals(3, reading.size());
    }
}
