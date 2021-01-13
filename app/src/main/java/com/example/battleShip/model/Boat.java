package com.example.battleShip.model;

import android.util.Log;

public enum Boat {
    CARRIER(5),
    BATTLESHIP(4),
    DESTROYER(3),
    SUBMARINE(3),
    PATROL(2),
    SEE(0);

    private final int length;

    Boat(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public char getChar() {
        switch (this) {
            case CARRIER:
                return 'C';
            case BATTLESHIP:
                return 'B';
            case DESTROYER:
                return 'D';
            case SUBMARINE:
                return 'S';
            case PATROL:
                return 'P';
            case SEE:
                return ' ';
            default:
                Log.e("BOAT", " getChar is weird...");
                return 'O';
        }
    }
}
