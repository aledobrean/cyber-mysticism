package com.cyber.mysticism.tarot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TarotDeckRepositoryTest {

    @Autowired
    private TarotDeckRepository tarotDeckRepository;

    @Test
    void getCards() {
        assertEquals(78, tarotDeckRepository.getCards().size());
    }
}