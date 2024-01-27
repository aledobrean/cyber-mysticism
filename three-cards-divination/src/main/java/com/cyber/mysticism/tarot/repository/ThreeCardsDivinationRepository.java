package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.model.ThreeCardsDivinationReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ThreeCardsDivinationRepository extends JpaRepository<ThreeCardsDivinationReading, Long> {
    @Query(value = "SELECT COUNT(r) FROM tarot_user u JOIN tarot_user_readings ur ON u.username = ur.tarot_user_username " +
            "JOIN three_cards_divination_reading r ON ur.readings_id = r.id " +
            "WHERE u.username = :username AND r.hash_code = :new_hash_code", nativeQuery = true)
    long countReadingsByHashCodeForUsername(@Param("new_hash_code") String newHashCode, @Param("username") String username);
}
