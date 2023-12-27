package com.cyber.mysticism.tarot.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Card(@JsonProperty("name") String name, @JsonProperty("number") Integer number,
                   @JsonProperty("arcana") String arcana, @JsonProperty("img") String img,
                   @JsonProperty("fortune_telling") List<String> fortuneTelling) {
}
