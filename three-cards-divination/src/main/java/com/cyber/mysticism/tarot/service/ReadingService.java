package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.Reading;
import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Reading service for all types of readings.
 */

@Service
public class ReadingService {
    private static final Logger logger = LoggerFactory.getLogger(ReadingService.class);
    private final ReadingRepository readingRepository;
    private final TarotUserService tarotUserService;

    @Autowired
    public ReadingService(ReadingRepository readingRepository, TarotUserService tarotUserService) {
        this.readingRepository = readingRepository;
        this.tarotUserService = tarotUserService;
    }

    /**
     * @return a list of all readings from a specific user
     */
    public List<Reading> getAllReadingsForUser(TarotUser tarotUser) throws UserNotFoundException {
        logger.info("event=retrieve_all_readings, user={}", tarotUser.getUsername());
        return readingRepository.findByUser(getTarotUser(tarotUser));
    }

    private TarotUser getTarotUser(TarotUser tarotUser) throws UserNotFoundException {
        return tarotUserService.findByUsernameAndEmail(tarotUser.getUsername(), tarotUser.getEmail());
    }
}
