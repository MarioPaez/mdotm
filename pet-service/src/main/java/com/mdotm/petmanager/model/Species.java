package com.mdotm.petmanager.model;

import lombok.Getter;

@Getter
public enum Species {
    DOG("Dog"),
    CAT("Cat"),
    RABBIT("Rabbit"),
    TURTLE("Turtle");

    private final String label;

    Species(String label) {
        this.label = label;
    }

}
