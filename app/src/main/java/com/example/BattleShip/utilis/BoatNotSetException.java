package com.example.BattleShip.utilis;

public class BoatNotSetException extends Exception {
    private String message;

    public BoatNotSetException() {
        super("Tile " + "" + "was already reached");
    }
}
