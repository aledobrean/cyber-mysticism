package com.cyber.mysticism.tarot.repository;

import com.cyber.mysticism.tarot.model.TarotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<TarotUser, String> {
    Optional<TarotUser> findByUsername(String username);

    Optional<TarotUser> findByEmail(String email);

    Optional<TarotUser> findByUsernameAndEmail(String username, String email);

    void deleteByUsername(String username);
}
