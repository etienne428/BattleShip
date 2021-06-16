package com.example.BattleShip.logic;

import android.graphics.Color;

import com.example.BattleShip.model.Boat;

public class TileStatus {
    private boolean targeted = false;
    private Boat boat;
    private final boolean opponentDefender;

    public TileStatus(Boat boat, boolean opponent) {
        this.boat = boat;
        this.opponentDefender = opponent;
    }

    public char getChar(boolean defend) {
        if (defend) {
            if (targeted && boat == Boat.SEE) {
                return 'O';
            } else {
                return boat.getChar();
            }
        } else {
            if (!targeted) {
                return ' ';
            } else if (boat != Boat.SEE) {
                return 'X';
            } else {
                return 'O';
            }
        }
    }

    public int getColor(boolean last) {
        if (!last) {
            if (isTouched()) {
                return Color.RED;
            } else if (targeted) {
                return Color.rgb(51, 161, 255);
            } else {
                return Color.rgb(51, 181, 229);
            }
        } else {
            if (isTouched()) {
                return Color.rgb(255, 10, 10);
            } else if (targeted) {
                return Color.rgb(71, 161, 255);
            } else {
                return Color.rgb(71, 181, 229);
            }
        }
    }

    private boolean isTouched() {
        return targeted && boat != Boat.SEE;
    }

    public void setTargeted() {
        targeted = true;
    }

    public boolean isTargeted() {
        return targeted;
    }

    public Boat getBoat() {
        return boat;
    }

    public boolean isOccupied() {
        return boat != Boat.SEE;
    }

    public boolean isOpponentDefender() {
        return opponentDefender;
    }
}
