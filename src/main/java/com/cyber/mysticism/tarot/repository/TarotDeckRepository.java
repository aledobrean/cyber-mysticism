package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.json.Deck;
import com.cyber.mysticism.tarot.repository.exceptions.TarotDeckAccessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Repository
public class TarotDeckRepository {

    private final List<Card> cards;
    private final ObjectMapper objectMapper;
    private final Resource resourceFile;

    @Autowired
    private TarotDeckRepository() throws TarotDeckAccessException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        this.resourceFile = resourceLoader.getResource("classpath:static/tarot-deck/tarot-cards.json");
        this.objectMapper = new ObjectMapper();
        this.cards = parseCards();
    }

    public List<Card> getCards() {
        return cards;
    }

    private List<Card> parseCards() throws TarotDeckAccessException {
        Deck deck = parseDeck();
        return deck.cards();
    }

    private Deck parseDeck() throws TarotDeckAccessException {
        try {
            return objectMapper.readValue(new BufferedReader(Files.newBufferedReader(Path.of(resourceFile.getURI()))), Deck.class);
        } catch (IOException e) {
            throw new TarotDeckAccessException("An error occurred while accessing data", e);
        }
    }
    }
}
