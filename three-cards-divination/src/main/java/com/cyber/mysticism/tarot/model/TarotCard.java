package com.cyber.mysticism.tarot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarot_card")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TarotCard implements Serializable {
    @Serial
    private static final long serialVersionUID = 1234567890L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    @JsonProperty("name")
    private String name;

    @Column(name = "number")
    @JsonProperty("number")
    private int number;

    @Column(name = "arcana")
    @JsonProperty("arcana")
    private String arcana;

    @Column(name = "img", unique = true)
    @JsonProperty("img")
    private String img;

    @Column(name = "fortune_telling")
    @JsonProperty("fortune_telling")
    private List<String> fortuneTelling = new ArrayList<>();

    public TarotCard() {
        // JPA
    }

    public TarotCard(String name, int number, String arcana, String img, List<String> fortuneTelling) {
        this.name = name;
        this.number = number;
        this.arcana = arcana;
        this.img = img;
        this.fortuneTelling = fortuneTelling;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getArcana() {
        return arcana;
    }

    public String getImg() {
        return img;
    }

    public List<String> getFortuneTelling() {
        return fortuneTelling;
    }
}
