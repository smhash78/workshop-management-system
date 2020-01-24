package com.java.workshop.model;

public enum Score {
    NOT_SET(0),
    VERY_BAD(1),
    BAD(2),
    NORMAL(3),
    GOOD(4),
    VERY_GOOD(5);

    int value;
    Score(int v) {
        value = v;
    }
    public int getValue() {
        return value;
    }
}
