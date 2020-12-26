package com.example.battleShip.utilis;

public class TileException extends Exception {
    public TileException(int status) {
        super("Tile " + status + " was already reached");
    }
}
