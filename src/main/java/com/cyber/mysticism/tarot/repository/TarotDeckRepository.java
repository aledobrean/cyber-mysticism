package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.json.Deck;
import com.cyber.mysticism.tarot.service.DivinationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Repository
public class TarotDeckRepository {

    private static TarotDeckRepository instance;
    private final List<Card> cards;
    private final ObjectMapper objectMapper;
    private final Resource resourceFile;

    @Autowired
    private TarotDeckRepository() throws DivinationException {
        this.resourceFile = new FileSystemResource("src/main/resources/static/tarot-deck/tarot-images.json");
        this.objectMapper = new ObjectMapper();
        this.cards = parseCards();
    }

    public static synchronized TarotDeckRepository getInstance() throws DivinationException {
        if (instance == null) {
            instance = new TarotDeckRepository();
        }
        return instance;
    }

    public List<Card> getCards() {
        return cards;
    }

    public Card getCardByNumber(Integer number) {
        return cards.stream().filter(card -> Objects.equals(number, card.number())).toList().getFirst();
    }

    private List<Card> parseCards() throws DivinationException {
        try {
            Deck deck = parseDeck();
            return deck.cards();
        } catch (IOException e) {
            throw new DivinationException("An error occurred while accessing data", e);
        }
    }

    private Deck parseDeck() throws IOException {
        return objectMapper.readValue(resourceFile.getFile(), Deck.class);
    }
}
