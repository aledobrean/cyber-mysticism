package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.Reading;
import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.service.exceptions.DivinationException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reading service for three cards divination type of readings.
 */

@Service
@Transactional
public class ThreeCardsDivinationService {

    public static final int READING_RETRY_COUNT = 5;
    private static final Logger logger = LoggerFactory.getLogger(ThreeCardsDivinationService.class);
    private static final String THREE_CARDS_DIVINATION = "three_cards_divination";
    private final ReadingRepository readingRepository;
    private final TarotCardService tarotCardService;
    private final TarotUserService tarotUserService;

    @Autowired
    public ThreeCardsDivinationService(ReadingRepository readingRepository, TarotCardService tarotCardService, TarotUserService tarotUserService) {
        this.readingRepository = readingRepository;
        this.tarotCardService = tarotCardService;
        this.tarotUserService = tarotUserService;
    }

    private static Collector<TarotCard, Object, Stream<TarotCard>> toShuffledStream() {
        return Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.shuffle(collected, ThreadLocalRandom.current());
            return collected.stream();
        });
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     * for a specific user, returning only non-duplicate readings
     */
    public ThreeCardsDivinationReading getReadingForUser(TarotUser tarotUser) throws DivinationException, UserNotFoundException {
        logger.info("event=performing_reading, type={}", THREE_CARDS_DIVINATION);
        return getUniqueReadingBasedOnUniqueReadingCode(getTarotUser(tarotUser), READING_RETRY_COUNT);
    }

    /**
     * @return a map containing a unique reading for a particular user in a recursive manner
     */
    private ThreeCardsDivinationReading getUniqueReadingBasedOnUniqueReadingCode(TarotUser tarotUser, int retryCount) throws DivinationException {
        ThreeCardsDivinationReading threeCardsDivinationReading = getReading();
        String uniqueReadingCode = getUniqueReadingCode(threeCardsDivinationReading);

        if (!readingRepository.findByUserAndUniqueReadingCode(tarotUser, uniqueReadingCode).isEmpty() && retryCount > 0) {
            // If duplicated, recursively call the method to get a new reading
            logger.info("event=reading_duplicated_for_user, type={}, user={}", THREE_CARDS_DIVINATION, tarotUser.getUsername());
            logger.info("event=reading_retry, retryCount={}", retryCount);
            return getUniqueReadingBasedOnUniqueReadingCode(tarotUser, retryCount - 1);
        } else if (retryCount == 0) {
            logger.info("event=reading_failed, retryCount={}, type={}, user={}", retryCount, THREE_CARDS_DIVINATION, tarotUser.getUsername());
            throw new DivinationException("Reading failed after " + retryCount + " attempts.");
        }

        saveReadingForUser(tarotUser, uniqueReadingCode, threeCardsDivinationReading);
        return threeCardsDivinationReading;
    }

    private TarotUser getTarotUser(TarotUser tarotUser) throws UserNotFoundException {
        return tarotUserService.findByUsernameAndEmail(tarotUser.getUsername(), tarotUser.getEmail());
    }

    private void saveReadingForUser(TarotUser tarotUser, String uniqueReadingCode, Reading reading) {
        reading.setUniqueReadingCode(uniqueReadingCode);
        reading.setUser(tarotUser);
        reading.setDate(Date.from(Instant.now()));

        readingRepository.save(reading);
        logger.info("event=reading_for_user_success, type={}, user={}", THREE_CARDS_DIVINATION, tarotUser.getUsername());
    }

    /**
     * @return a map that contains the reading in the form past, present and future
     */
    private ThreeCardsDivinationReading getReading() throws DivinationException {
        List<TarotCard> extractedCards = new ArrayList<>();
        ThreeCardsDivinationReading reading = new ThreeCardsDivinationReading();
        Optional<TarotCard> majorArcana = extractMajorArcana();
        TarotCard majorArcanaCard = majorArcana.orElseThrow(() -> new DivinationException("No Major Arcana card was returned."));
        if (majorArcanaCard != null) {
            extractedCards.add(majorArcanaCard);
            extractedCards.addAll(extractTwoRandomCards(majorArcanaCard.getName()));
            extractedCards = extractedCards.stream().collect(toShuffledStream()).toList();

            if (extractedCards.size() == 3) {
                reading = new ThreeCardsDivinationReading(extractedCards.get(0), extractedCards.get(1), extractedCards.get(2));
            } else {
                logger.info("event=reading_failure, status=error, type={}", THREE_CARDS_DIVINATION);
                throw new DivinationException("Three Cards Divination reading failed.");
            }
        }
        return reading;
    }

    private Optional<TarotCard> extractMajorArcana() {
        Stream<TarotCard> majorArcanaDeck = getMajorArcanaCards().stream();
        return majorArcanaDeck.collect(toShuffledStream()).findFirst();
    }

    private List<TarotCard> getMajorArcanaCards() {
        logger.info("event=retrieve_major_arcana_cards, type={}", THREE_CARDS_DIVINATION);
        return tarotCardService.getMajorArcanaCards();
    }

    public List<TarotCard> getCards() {
        logger.info("event=retrieve_cards, type={}", THREE_CARDS_DIVINATION);
        return tarotCardService.getCards();
    }

    private List<TarotCard> extractTwoRandomCards(String duplicatedCardName) {
        Stream<TarotCard> cards = getCards().stream();
        return cards.filter(card -> !duplicatedCardName.equals(card.getName())).collect(toShuffledStream()).limit(2).toList();
    }

    private String getUniqueReadingCode(ThreeCardsDivinationReading reading) {
        return reading.generateUniqueReadingCode();
    }
}
