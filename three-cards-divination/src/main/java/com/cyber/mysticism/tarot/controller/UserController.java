package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotUser;
import com.cyber.mysticism.tarot.service.UserService;
import com.cyber.mysticism.tarot.service.exceptions.UserAlreadyExistsException;
import com.cyber.mysticism.tarot.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<TarotUser> findAll() {
        return userService.findAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TarotUser create(@RequestBody TarotUser tarotUser) throws UserAlreadyExistsException {
        return userService.save(tarotUser);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{username}")
    public void deleteById(@PathVariable String username) throws UserNotFoundException {
        userService.deleteByUsername(username);
    }
}
