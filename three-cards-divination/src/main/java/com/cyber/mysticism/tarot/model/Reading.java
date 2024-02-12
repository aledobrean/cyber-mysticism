package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "reading")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "reading_type", discriminatorType = DiscriminatorType.STRING)
public class Reading {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "unique_reading_code")
    private String uniqueReadingCode;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private TarotUser user;

    @Column(name = "date")
    @JsonProperty("date")
    private Date date;

    public Reading() {
        // JPA
    }

    public void setUser(TarotUser user) {
        this.user = user;
    }

    public void setUniqueReadingCode(String uniqueReadingCode) {
        this.uniqueReadingCode = uniqueReadingCode;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}