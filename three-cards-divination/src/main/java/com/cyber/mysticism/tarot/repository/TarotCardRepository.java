package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.model.TarotCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TarotCardRepository extends JpaRepository<TarotCard, Long> {
}
