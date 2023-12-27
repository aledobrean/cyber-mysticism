package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.repository.TarotDeckRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ThreeCardsDivinationService {

    public static final String THREE_CARDS_DIVINATION = "three_cards_divination";
    public static final String MAJOR_ARCANA = "Major Arcana";
    private static final Logger logger = LoggerFactory.getLogger(ThreeCardsDivinationService.class);
    private final TarotDeckRepository tarotDeckRepository;

    @Autowired
    public ThreeCardsDivinationService() throws DivinationException {
        this.tarotDeckRepository = TarotDeckRepository.getInstance();
    }

    private static Collector<Card, Object, Stream<Card>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected);
            return collected.stream();
        });
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     */
    public Map<String, Card> getReading() {
        List<Card> extractedCards = new ArrayList<>();
        Map<String, Card> reading = new HashMap<>();
        Card majorArcanaCard = extractMajorArcana();

        extractedCards.add(majorArcanaCard);
        extractedCards.addAll(extractCardsWithFilter(majorArcanaCard.number()));
        extractedCards = shuffleCards(extractedCards);

        reading.put("past", extractedCards.get(0));
        reading.put("present", extractedCards.get(1));
        reading.put("future", extractedCards.get(2));

        return reading;
    }

    public List<Card> getCards() {
        logger.info("event=retrieve_cards, type={}", THREE_CARDS_DIVINATION);
        return tarotDeckRepository.getCards();
    }

    public List<Card> getMajorArcanaCards() {
        logger.info("event=retrieve_major_arcana_cards, type={}", THREE_CARDS_DIVINATION);
        return getCards().stream().filter(card -> MAJOR_ARCANA.equals(card.arcana())).toList();
    }

    /**
     * @return one major arcana card from the deck.
     */
    private Card extractMajorArcana() {
        Stream<Card> majorArcanaDeck = getMajorArcanaCards().stream();

        return majorArcanaDeck.collect(toShuffledStream()).findFirst().orElse(tarotDeckRepository.getCardByNumber(0));
    }

    /**
     * Extract two random cards from the deck.
     *
     * @param duplicatedCardNumber represents a card that was already extracted, avoiding duplicates
     */
    private List<Card> extractCardsWithFilter(Integer duplicatedCardNumber) {
        Stream<Card> deck = getCards().stream();

        return deck.filter(card -> !duplicatedCardNumber.equals(card.number())).collect(toShuffledStream()).limit(2).toList();
    }

    private List<Card> shuffleCards(List<Card> cards) {
        return cards.stream().collect(toShuffledStream()).toList();
    }
}
