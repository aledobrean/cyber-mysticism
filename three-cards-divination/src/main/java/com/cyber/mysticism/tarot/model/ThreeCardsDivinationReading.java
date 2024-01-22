package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "three_cards_divination_reading")
public class ThreeCardsDivinationReading {
    @Id
    @GeneratedValue
    private Long id;

    @JsonProperty("hash_code")
    private String hashCode;

    public ThreeCardsDivinationReading() {
        // JPA
    }

    public ThreeCardsDivinationReading(String hashCode) {
        this.hashCode = hashCode;
    }

    public String getHashCode() {
        return hashCode;
    }
}
