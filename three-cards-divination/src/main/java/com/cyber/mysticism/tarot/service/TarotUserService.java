package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.repository.ReadingRepository;
import com.cyber.mysticism.tarot.repository.UserRepository;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarotUserService {

    private static final Logger logger = LoggerFactory.getLogger(TarotUserService.class);

    private final UserRepository userRepository;
    private final ReadingRepository readingRepository;

    @Autowired
    public TarotUserService(UserRepository userRepository, ReadingRepository readingRepository) {
        this.userRepository = userRepository;
        this.readingRepository = readingRepository;
    }

    public List<TarotUser> findAll() {
        logger.info("event=retrieve_all_users");
        return userRepository.findAll();
    }

    public Optional<TarotUser> findByUsername(String username) {
        logger.info("event=retrieve_user_by_username, username={}", username);
        return userRepository.findByUsername(username);
    }

    public Optional<TarotUser> findByEmail(String email) {
        logger.info("event=retrieve_user_by_email, email={}", email);
        return userRepository.findByEmail(email);
    }

    public TarotUser findByUsernameAndEmail(String username, String email) throws UserNotFoundException {
        logger.info("event=retrieve_user_by_username_and_email, username={}, email={}", username, email);
        Optional<TarotUser> retrievedUser = userRepository.findByUsernameAndEmail(username, email);

        if (retrievedUser.isEmpty()) {
            logger.info("event=reading_for_user_failed, status=error, reason=invalid_user, user={}", username);
            throw new UserNotFoundException("Cannot perform reading, user not found");
        }
        return retrievedUser.get();
    }

    public TarotUser save(TarotUser tarotUser) throws UserAlreadyExistsException {
        if (tarotUser.getUsername() != null && findByUsername(tarotUser.getUsername()).isPresent()) {
            logger.info("event=save_user, status=error, reason=username_exists, username={}", tarotUser.getUsername());
            throw new UserAlreadyExistsException("Username already exists, choose a different one.");
        } else if (tarotUser.getEmail() != null && findByEmail(tarotUser.getEmail()).isPresent()) {
            logger.info("event=save_user, status=error, reason=email_exists, email={}", tarotUser.getEmail());
            throw new UserAlreadyExistsException("Email already exists, choose a different one.");
        } else {
            logger.info("event=save_user, status=success, username={}", tarotUser.getUsername());
            return userRepository.save(tarotUser);
        }
    }

    public void deleteByUsername(String username) throws UserNotFoundException {
        Optional<TarotUser> user = findByUsername(username);
        if (username != null && user.isPresent()) {
            readingRepository.deleteByUser(user.get());
            logger.info("event=delete_user_readings, status=success, username={}", username);
            userRepository.deleteByUsername(username);
            logger.info("event=delete_user, status=success, username={}", username);
        } else {
            logger.info("event=delete_user, status=error, reason=user_not_found");
            throw new UserNotFoundException("Couldn't delete username='" + username + "', user not found.");
        }
    }
}
