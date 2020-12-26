package com.example.battleShip.model;

import android.graphics.Color;
import android.util.Log;

import com.example.battleShip.utilis.TileException;

/**
 * Represent the state of a tile.
 * <p>
 * Last digit 0 means clear (enemy hasn't targeted it), while
 * last digit 1 means targeted (a miss if no ship, a touch if yes).
 */
public enum TileState {
    // The rest of the Tiles,
    SEE_CLEAR(0, ' ', false),
    // TODO: Make sure this one can be deleted
    SEE_TOUCH(1, 'X', true),
    SEE_MISS(5, 'O', false),

    // Opponent's tiles
    OPP_CLEAR(2, ' ', false),
    OPP_TOUCH(3, 'X', true),
    OPP_MISS(4, 'O', true),

    // 5 Tiles
    CARRIER_CLEAR(10, 'C', false),
    CARRIER_TOUCH(11, 'C', true),

    // 4 Tiles
    BATTLESHIP_CLEAR(20, 'B', false),
    BATTLESHIP_TOUCH(21, 'B', true),

    // 3 Tiles
    DESTROYER_CLEAR(30, 'D', false),
    DESTROYER_TOUCH(31, 'D', true),

    // 3 Tiles
    SUBMARINE_CLEAR(40, 'S', false),
    SUBMARINE_TOUCH(41, 'S', true),

    // 2 Tiles (Patrol Boat)
    PATROL_CLEAR(50, 'P', false),
    PATROL_TOUCH(51, 'P', true);

    private final int status;
    private final char character;
    private final boolean touched;

    TileState(int status, char character, boolean touched) {
        this.status = status;
        this.character = character;
        this.touched = touched;
    }

    public int getStatus() {
        return status;
    }

    public char getCharacter() {
        return character;
    }

    /**
     * Has the tile been targeted.
     *
     * @return true is yes, even if it was a miss.
     */
    public boolean isTargeted() {
        return touched;
    }

    /**
     * Does the tile contains a boat that has been targeted.
     *
     * @return true if tile contains a boat and was targeted
     */
    public boolean isTouched() {
        Log.i("TOUCHED", "status = " + status);
        return touched && !(status == 1 || status == 4);
    }

    public TileState setMissed() throws TileException {
        if (status == 2) {
            return OPP_MISS;
        } else if (status == 0) {
            return SEE_MISS;
        } else {
            Log.i("SET_MISSED", status + " was a miss!!!!!!!!!!!!!!!!!!");
            throw new TileException(status);
        }
    }

    /**
     * Set a tile as touched by a bomb
     *
     * @return the correspondent touched tile
     * @throws TileException if tile was already targeted
     */
    public TileState setTouched() throws TileException {
        switch (status) {
            case 0:
                return SEE_TOUCH;
            case 2:
                return OPP_TOUCH;
            case 10:
                return CARRIER_TOUCH;
            case 20:
                return BATTLESHIP_TOUCH;
            case 30:
                return DESTROYER_TOUCH;
            case 40:
                return SUBMARINE_TOUCH;
            case 50:
                return PATROL_TOUCH;
            default:
                throw new TileException(status);
        }
    }

    public static TileState getTile(int status) throws TileException {
        switch (status) {
            case 0:
                return SEE_CLEAR;
            case 1:
                return SEE_TOUCH;
            case 10:
                return CARRIER_CLEAR;
            case 11:
                return CARRIER_TOUCH;
            case 20:
                return BATTLESHIP_CLEAR;
            case 21:
                return BATTLESHIP_TOUCH;
            case 30:
                return DESTROYER_CLEAR;
            case 31:
                return DESTROYER_TOUCH;
            case 40:
                return SUBMARINE_CLEAR;
            case 41:
                return SUBMARINE_TOUCH;
            case 50:
                return PATROL_CLEAR;
            case 51:
                return PATROL_TOUCH;

            case 2:
                return OPP_CLEAR;
            case 3:
                return OPP_TOUCH;
            case 4:
                return OPP_MISS;
            default:
                throw new TileException(status);
        }
    }

    public int getColor() throws TileException {
        switch (status) {
            // Not targeted
            case 0:
            case 2:
                return Color.rgb(51, 181, 229);
            // Miss
            case 4:
            case 5:
                return Color.rgb(51, 161, 251);
            // Boat clear
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
                return Color.rgb(51, 251, 161);
            // Touched
            case 1:
            case 3:
            case 11:
            case 21:
            case 31:
            case 41:
            case 51:
                return Color.RED;

            default:
                throw new TileException(status);
        }
    }

    public int getColorLast() throws TileException {
        switch (status) {
            // Not targeted
            case 0:
            case 2:
                return Color.rgb(51, 251, 229);
            // Miss
            case 4:
            case 5:
                return Color.rgb(51, 231, 251);
            // Touched
            case 1:
            case 3:
            case 11:
            case 21:
            case 31:
            case 41:
            case 51:
                return Color.rgb(251, 40, 0);

            // Boat clear
            case 10:
            case 20:
            case 30:
            case 40:
            case 50:
//                return Color.rgb(51, 251, 161);
            default:
                throw new TileException(status);
        }
    }

    /**
     * Assert that a tile is free.
     * <p>
     * Mainly for boat disposal step.
     *
     * @return true if boat can be set.
     */
    public boolean isOccupied() {
        return status != 0;
    }

    public Boat getBoat() throws TileException {
        switch (status) {
            case 0:
            case 1:
            case 2:
            case 4:
                return Boat.SEE;
            case 10:
            case 11:
                return Boat.CARRIER;
            case 20:
            case 21:
                return Boat.BATTLESHIP;
            case 30:
            case 31:
                return Boat.DESTROYER;
            case 40:
            case 41:
                return Boat.SUBMARINE;
            case 50:
            case 51:
                return Boat.PATROL;

            case 3:
            default:
                throw new TileException(status);
        }
    }

    public TileState setTouchedBoat(Boat attempt) throws TileException {
        switch (attempt) {
            case CARRIER:
                return CARRIER_TOUCH;
            case BATTLESHIP:
                return BATTLESHIP_TOUCH;
            case DESTROYER:
                return DESTROYER_TOUCH;
            case SUBMARINE:
                return SUBMARINE_TOUCH;
            case PATROL:
                return PATROL_TOUCH;
            default:
                throw new TileException(attempt.getLength());
        }
    }
}