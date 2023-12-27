package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.json.Card;
import com.cyber.mysticism.tarot.service.ThreeCardsDivinationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ThreeCardsDivinationController {

    private final ThreeCardsDivinationService threeCardsDivinationService;

    @Autowired
    public ThreeCardsDivinationController(ThreeCardsDivinationService threeCardsDivinationService) {
        this.threeCardsDivinationService = threeCardsDivinationService;
    }

    @GetMapping("/three-cards-divination")
    public Map<String, Card> performReading() {
        return threeCardsDivinationService.getReading();
    }
}
