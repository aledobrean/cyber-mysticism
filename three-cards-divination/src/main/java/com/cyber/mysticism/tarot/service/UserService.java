package com.cyber.mysticism.tarot.service;

import com.cyber.mysticism.tarot.model.TarotUser;
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
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<TarotUser> findAll() {
        logger.info("event=retrieve_all_users");
        return userRepository.findAll();
    }

    public Optional<TarotUser> findByUsername(String username) {
        logger.info("event=retrieve_user_by_username, username={}", username);
        return userRepository.findById(username);
    }

    public Optional<TarotUser> findByEmail(String email) {
        logger.info("event=retrieve_user_by_email, email={}", email);
        return userRepository.findByEmail(email);
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
        if (username != null && findByUsername(username).isPresent()) {
            logger.info("event=delete_user, status=success, username={}", username);
            userRepository.deleteById(username);
        } else {
            logger.info("event=delete_user, status=error, reason=user_not_found");
            throw new UserNotFoundException("Couldn't delete username='" + username + "', user not found.");
        }
    }
}
