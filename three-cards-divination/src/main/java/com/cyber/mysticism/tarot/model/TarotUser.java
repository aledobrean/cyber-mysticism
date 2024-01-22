package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tarot_user")
public class TarotUser implements Serializable {
    @Id
    @Column(name = "username", nullable = false, unique = true)
    @JsonProperty("username")
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("readings")
    private Set<ThreeCardsDivinationReading> readings = new HashSet<>();

    public TarotUser() {
        // for JPA
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<ThreeCardsDivinationReading> getReadings() {
        return readings;
    }

    public void setReadings(Set<ThreeCardsDivinationReading> readings) {
        this.readings = readings;
    }
}