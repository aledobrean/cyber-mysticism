package com.cyber.mysticism.tarot.controller;

import com.cyber.mysticism.tarot.model.TarotCard;
import com.cyber.mysticism.tarot.service.TarotCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TarotDeckController {
    private final TarotCardService tarotCardService;

    @Autowired
    public TarotDeckController(TarotCardService tarotCardService) {
        this.tarotCardService = tarotCardService;
    }

    @Operation(summary = "Get all cards", description = "Retrieve all cards")
    @ApiResponse(responseCode = "200", description = "Retrieved cards successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarotCard.class)))
    @GetMapping("/cards")
    public List<TarotCard> getCards() {
        return tarotCardService.getCards();
    }

    @Operation(summary = "Get all major arcana cards", description = "Retrieve all major arcana cards")
    @ApiResponse(responseCode = "200", description = "Retrieved major arcana cards successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TarotCard.class)))
    @GetMapping("/cards/major-arcana")
    public List<TarotCard> getMajorArcanaCards() {
        return tarotCardService.getMajorArcanaCards();
    }
}
