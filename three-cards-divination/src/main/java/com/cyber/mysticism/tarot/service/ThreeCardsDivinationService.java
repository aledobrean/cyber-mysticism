package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.repository.TarotDeckRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    public ThreeCardsDivinationService(TarotDeckRepository tarotDeckRepository) {
        this.tarotDeckRepository = tarotDeckRepository;
    }

    private static Collector<Card, Object, Stream<Card>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected, ThreadLocalRandom.current());
            return collected.stream();
        });
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     */
    public Map<String, Card> getReading() throws DivinationException {
        List<Card> extractedCards = new ArrayList<>();
        Map<String, Card> reading = new HashMap<>();
        Optional<Card> majorArcana = extractMajorArcana();
        Card majorArcanaCard = majorArcana.orElseThrow(() -> new DivinationException("No Major Arcana cards was returned."));
        if (majorArcanaCard != null) {
            extractedCards.add(majorArcanaCard);
            extractedCards.addAll(extractCardsWithFilter(majorArcanaCard.name()));
            extractedCards = extractedCards.stream().collect(toShuffledStream()).toList();

            if (extractedCards.size() == 3) {
                reading.put("past", extractedCards.get(0));
                reading.put("present", extractedCards.get(1));
                reading.put("future", extractedCards.get(2));
            } else {
                logger.info("event=reading_failure, type=three_cards_divination");
                throw new DivinationException("Three Cards Tarot reading failed.");
            }
        }

        return reading;
    }

    private List<Card> getCards() {
        logger.info("event=retrieve_cards, type={}", THREE_CARDS_DIVINATION);
        return tarotDeckRepository.getCards();
    }

    /**
     * @return one major arcana card from the deck of cards.
     */
    private Optional<Card> extractMajorArcana() {
        Stream<Card> majorArcanaDeck = getMajorArcanaCards().stream();
        return majorArcanaDeck.collect(toShuffledStream()).findFirst();
    }

    private List<Card> getMajorArcanaCards() {
        logger.info("event=retrieve_major_arcana_cards, type={}", THREE_CARDS_DIVINATION);
        return getCards().stream().filter(card -> MAJOR_ARCANA.equals(card.arcana())).toList();
    }

    /**
     * Extract two random cards from the deck of cards.
     *
     * @param duplicatedCardName represents a card that was already extracted, avoiding duplicates
     */
    private List<Card> extractCardsWithFilter(String duplicatedCardName) {
        Stream<Card> cards = getCards().stream();
        return cards.filter(card -> !duplicatedCardName.equals(card.name())).collect(toShuffledStream()).limit(2).toList();
    }
}
