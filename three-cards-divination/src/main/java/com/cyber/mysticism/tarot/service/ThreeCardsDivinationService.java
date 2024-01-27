package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.repository.TarotDeckRepository;
import com.cyber.mysticism.tarot.repository.ThreeCardsDivinationRepository;
import com.cyber.mysticism.tarot.repository.UserRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final UserRepository userRepository;
    private final ThreeCardsDivinationRepository threeCardsDivinationRepository;

    @Autowired
    public ThreeCardsDivinationService(TarotDeckRepository tarotDeckRepository, UserRepository userRepository,
                                       ThreeCardsDivinationRepository threeCardsDivinationRepository) {
        this.tarotDeckRepository = tarotDeckRepository;
        this.userRepository = userRepository;
        this.threeCardsDivinationRepository = threeCardsDivinationRepository;
    }

    private static Collector<Card, Object, Stream<Card>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected, ThreadLocalRandom.current());
            return collected.stream();
        });
    }

    private static String convertReadingMapToJsonString(Map<String, Card> threeCardsDivinationReading) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(threeCardsDivinationReading);
        } catch (JsonProcessingException e) {
            logger.error("Error while converting Map to JSON String.");
            return null;
        }
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     * for a specific user, returning only non-duplicate readings
     */
    public Map<String, Card> getReadingForUser(String username, String email) throws DivinationException, UserNotFoundException {
        TarotUser retrievedUser = getTarotUser(username, email);

        return getUniqueReadingBasedOnHashCode(retrievedUser);
    }

    private TarotUser getTarotUser(String username, String email) throws UserNotFoundException {
        Optional<TarotUser> retrievedUser = userRepository.findById(username);
        Optional<TarotUser> retrievedUserByEmail = userRepository.findByEmail(email);
        if (retrievedUser.isEmpty() || retrievedUserByEmail.isEmpty() ||
                !retrievedUser.get().equals(retrievedUserByEmail.get())) {
            logger.info("event=reading_for_user_failed, status=error, reason=invalid_user, type={}, user={}",
                    THREE_CARDS_DIVINATION, username);
            throw new UserNotFoundException("Cannot perform reading, user not found");
        }
        return retrievedUser.get();
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     */
    private Map<String, Card> getReading() throws DivinationException {
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
                logger.info("event=reading_failure, status=error, type={}", THREE_CARDS_DIVINATION);
                throw new DivinationException("Three Cards Tarot reading failed.");
            }
        }
        logger.info("event=reading_success, type={}", THREE_CARDS_DIVINATION);
        return reading;
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

    private List<Card> getCards() {
        logger.info("event=retrieve_cards, type={}", THREE_CARDS_DIVINATION);
        return tarotDeckRepository.getCards();
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

    /**
     * @return a map containing a unique reading for a particular user in a recursive manner
     */
    private Map<String, Card> getUniqueReadingBasedOnHashCode(TarotUser retrievedUser) throws DivinationException {
        Map<String, Card> threeCardsDivinationReading = getReading();
        String hashCode = getHashCode(threeCardsDivinationReading);

        if (threeCardsDivinationRepository.countReadingsByHashCodeForUsername(hashCode, retrievedUser.getUsername()) != 0) {
            // If duplicated, recursively call the method to get a new reading
            logger.info("event=reading_duplicated_for_user, type={}, user={}", THREE_CARDS_DIVINATION, retrievedUser.getUsername());
            return getUniqueReadingBasedOnHashCode(retrievedUser);
        }

        saveReadingForUser(retrievedUser, hashCode, threeCardsDivinationReading);

        return threeCardsDivinationReading;
    }

    /**
     * Save new hash_code and corresponding reading to DB
     */
    private void saveReadingForUser(TarotUser retrievedUser, String hashCode, Map<String, Card> threeCardsDivinationReading) {
        Set<ThreeCardsDivinationReading> retrievedUserReadings = retrievedUser.getReadings();
        retrievedUserReadings.add(new ThreeCardsDivinationReading(hashCode, convertReadingMapToJsonString(threeCardsDivinationReading)));
        retrievedUser.setReadings(retrievedUserReadings);
        userRepository.save(retrievedUser);

        logger.info("event=reading_for_user_success, type={}, user={}", THREE_CARDS_DIVINATION, retrievedUser.getUsername());
    }

    private String getHashCode(Map<String, Card> reading) {
        return String.valueOf(Objects.hashCode(reading));
    }
}
