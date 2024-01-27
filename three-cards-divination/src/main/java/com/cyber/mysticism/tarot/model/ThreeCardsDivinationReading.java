package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "three_cards_divination_reading")
public class ThreeCardsDivinationReading {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "hash_code")
    @JsonProperty("hash_code")
    private String hashCode;

    @Column(name = "reading", length = 1500)
    @JsonProperty("reading")
    private String reading;

    public ThreeCardsDivinationReading() {
        // JPA
    }

    public ThreeCardsDivinationReading(String hashCode, String reading) {
        this.hashCode = hashCode;
        this.reading = reading;
    }
}
