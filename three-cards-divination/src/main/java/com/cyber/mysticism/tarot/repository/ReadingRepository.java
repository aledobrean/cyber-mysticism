package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.model.Reading;
import com.cyber.mysticism.tarot.model.TarotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ReadingRepository extends JpaRepository<Reading, Long> {
    List<Reading> findByUserAndUniqueReadingCode(TarotUser user, String uniqueReadingCode);

    List<Reading> findByUser(TarotUser user);

    void deleteByUser(TarotUser user);
}
