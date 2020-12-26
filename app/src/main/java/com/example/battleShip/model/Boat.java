package com.example.battleShip.model;

public enum Boat {
    CARRIER(5),
    BATTLESHIP(4),
    DESTROYER(3),
    SUBMARINE(3),
    PATROL(2),
    SEE(0),
    MISSED(0);

    private final int length;

    Boat(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
