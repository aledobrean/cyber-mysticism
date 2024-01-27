package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.model.TarotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<TarotUser, String> {
    Optional<TarotUser> findByEmail(String email);
}
