package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("three_cards_divination")
public class ThreeCardsDivinationReading extends Reading {

    @ManyToOne
    @JoinColumn(name = "past")
    @JsonProperty("past")
    private TarotCard past;

    @ManyToOne
    @JoinColumn(name = "present")
    @JsonProperty("present")
    private TarotCard present;

    @ManyToOne
    @JoinColumn(name = "future")
    @JsonProperty("future")
    private TarotCard future;

    public ThreeCardsDivinationReading() {
        // JPA
    }

    public ThreeCardsDivinationReading(TarotCard past, TarotCard present, TarotCard future) {
        this.past = past;
        this.present = present;
        this.future = future;
    }

    public TarotCard getPast() {
        return past;
    }

    public TarotCard getPresent() {
        return present;
    }

    public TarotCard getFuture() {
        return future;
    }

    public String generateUniqueReadingCode() {
        return past.getName() + present.getName() + future.getName();
    }
}
