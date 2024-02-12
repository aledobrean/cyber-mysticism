package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.repository.TarotCardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TarotCardService {
    private static final Logger logger = LoggerFactory.getLogger(TarotCardService.class);
    private static final String MAJOR_ARCANA = "Major Arcana";

    private final TarotCardRepository tarotCardRepository;

    @Autowired
    public TarotCardService(TarotCardRepository tarotCardRepository) {
        this.tarotCardRepository = tarotCardRepository;
    }

    public List<TarotCard> getCards() {
        logger.info("event=retrieve_all_cards");
        return tarotCardRepository.findAll();
    }

    public List<TarotCard> getMajorArcanaCards() {
        logger.info("event=retrieve_all_major_arcana_cards");
        return getCards().stream().filter(card -> MAJOR_ARCANA.equals(card.getArcana())).toList();
    }
}
