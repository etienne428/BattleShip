package com.example.BattleShip.logic;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.example.BattleShip.model.Boat;
import com.example.BattleShip.model.GameActivity;

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
                return ' ';
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
        String text = "";
        if (!last) {
            if (isTouched()) {
                text = "last touched";
                Log.d("GETCOLOR",text);
                return Color.RED;
            } else if (targeted) {
                text = "last target";
                Log.d("GETCOLOR",text);
                return Color.rgb(51, 161, 255);
            } else {
                text = "last void";
                Log.d("GETCOLOR",text);
                return Color.rgb(51, 181, 229);
            }
        } else {
            if (isTouched()) {
                text = "nolast touched";
                Log.d("GETCOLOR",text);
                return Color.rgb(255, 10, 10);
            } else if (targeted) {
                text = "nolast target";
                Log.d("GETCOLOR",text);
                return Color.rgb(71, 161, 255);
            } else {
                text = "nolast void";
                return Color.rgb(71, 181, 229);
            }
        }
    }

    public int getColorDefend(boolean last) {
        String text = "";
        if (!last) {
            if (isTouched()) {
                text = "last touched";
                Log.d("GETCOLOR",text);
                return Color.RED;
            } else if (targeted) {
                text = "last target";
                Log.d("GETCOLOR",text);
                return Color.rgb(51, 161, 255);
            } else {
                text = "last void";
                Log.d("GETCOLOR",text);
                return Color.rgb(51, 181, 229);
            }
        } else {
            if (isTouched()) {
                text = "nolast touched";
                Log.d("GETCOLOR",text);
                return Color.rgb(255, 10, 10);
            } else if (targeted) {
                text = "nolast target";
                Log.d("GETCOLOR",text);
                return Color.rgb(71, 161, 255);
            } else {
                text = "nolast void";
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
