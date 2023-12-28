package com.cyber.mysticism.tarot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Deck(@JsonProperty("description") String description,
                   @JsonProperty("cards") List<Card> cards) {
}
