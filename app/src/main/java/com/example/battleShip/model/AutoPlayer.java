package com.example.battleShip.model;

import android.util.Log;

import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.utilis.BoatNotSetException;
import com.example.battleShip.utilis.TileException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class AutoPlayer {

    private final GameActivity game;
    private final int columns;
    private final int rows;
    private TileStatus[] autoTiles;
    private final Random random = new Random();
    private Boat lastBoat;
    private int lastTile = -1;
    private final LinkedList<Integer> attempts = new LinkedList<>();

    // Direction to go for next attempt in case of a TOUCH.
    //  1, 2, 3 and 4 == west, north, east, south.
    private int directionOfNextAttempt = 1;

    /**
     * The equivalent of GameFrame, interface between AClient and AWaterGrid.
     *
     * @param game the activity
     */
    public AutoPlayer(GameActivity game, int columns, int rows) {
        this.game = game;
        this.columns = columns;
        this.rows = rows;
    }

    /**
     * Choose where the computer sets its boats.
     */
    void setBoatOnDefault() {
        // For each of the 5 boats
        for (int i = 0; i < 5; i++) {
            //orientation in the grid
            boolean northSouth = random.nextBoolean();
            // Number of tiles for the new boat
            int shipLength = Boat.values()[i].getLength();
            try {
                setBoat(northSouth, shipLength, i);
            } catch (BoatNotSetException e) {
                // If chosen place was occupied, try again
                i--;
            }
        }
    }

    /**
     * Set a boat.
     * Extracted from setBoatOnDefault() for clarity.
     *
     * @param northSouth the orientation
     * @param shipLength the number of tiles for the ship
     * @param shipIndex  the index of the boat in the Boat enum
     * @throws BoatNotSetException if a tile was occupied
     */
    private void setBoat(boolean northSouth, int shipLength, int shipIndex) throws BoatNotSetException {
        TileStatus state;
        // Coordinate of the first tile
        int line;
        int column;

        int gap = 1;
        if (northSouth) {
            column = random.nextInt(columns);
            line = random.nextInt(rows - shipLength);
            gap *= columns;
        } else {
            column = random.nextInt(columns - shipLength);
            line = random.nextInt(rows);
        }
        // Index of the first tile
        int tile = line * columns + column;
        // Check that tiles are free
        boatCanBeSet(tile, shipLength, northSouth);
        // The TileState for the new boat
        state = new TileStatus(Boat.values()[shipIndex], false);
        // Fill the tiles
        for (int j = 0; j < shipLength; j++) {
            autoTiles[tile + (j * gap)] = state;
        }
    }

    /**
     * Check that place is free for new boat.
     * Acts like returning a boolean, but throws an exception if false instead.
     *
     * @param tile       the first tile
     * @param shipLength the length of the ship
     * @param northSouth true if boat spans across several rows but one column
     * @throws BoatNotSetException if one tile is already taken
     */
    private void boatCanBeSet(int tile, int shipLength, boolean northSouth) throws BoatNotSetException {
        for (int i = 0; i < shipLength; i++) {
            if (northSouth) {
                if (autoTiles[tile + (i * columns)].isOccupied()) {
                    throw new BoatNotSetException();
                }
            } else {
                if (autoTiles[tile + i].isOccupied()) {
                    throw new BoatNotSetException();
                }
            }
        }
    }

    /**
     * The computer's attempt.
     */
    public void makeAttempt() {
        try {
            do {
                lastTile = random.nextInt(rows * columns);
            } while (attempts.contains(lastTile));
            // Make the attempt
            game.checkComputerAttempt(lastTile);
            attempts.add(lastTile);
        } catch (TileException e) {
            e.printStackTrace();
        }
/*        Log.i("BOAT_IS", "Boat is " + lastBoat
                + ", direction = " + directionOfNextAttempt
                + " and lastTile = " + lastTile);
        int lastTarget = lastTile;
        try {
            if (true) {//lastTile == -1) {
                // Get a random number
                do {
                    lastTile = random.nextInt(rows * columns);
                } while (attempts.contains(lastTile));
                // Make the attempt
                lastBoat = game.checkComputerAttempt(lastTile);
                attempts.add(lastTile);
                // If MISS, next attempt will be random too.
                if (lastBoat == Boat.SEE) {
                    lastTile = -1;
                }
                Log.i("BOAT_IS", "33Boat is " + lastBoat
                        + ", direction = " + directionOfNextAttempt
                        + " and lastTile = " + lastTile);
            } else {
                // Last attempt was successful
                switch (directionOfNextAttempt) {
                    case 1:
                        // East
                        lastTile--;
                        break;
                    case 2:
                        // North
                        lastTile -= columns;
                        break;
                    case 3:
                        lastTile++;
                        break;
                    case 4:
                        lastTile += columns;
                        break;
                    default:
                        Log.e("DIRECTION_OF_NEXT_ATT", "Error : directionOfNextAttempt = " + directionOfNextAttempt);
                        directionOfNextAttempt = 1;
                        makeAttempt();
                }
                // If tile index is valid
                if (lastTile >= 0 && lastTile < columns * rows) {
                    // Make the attempt
                    try {
                        if (attempts.contains(lastTile)) {
                            Log.e("BOAT_IS", "Boat!!!!!!!!!!!!!!!!! is " + lastBoat
                                    + ", direction = " + directionOfNextAttempt
                                    + " and lastTile = " + lastTile);    // If MISSED, set lastTile back and change direction
                            directionOfNextAttempt++;
                            if (directionOfNextAttempt > 4) {
                                lastTile = -1;
                                makeAttempt();
                            }
                        }
                        lastBoat = game.checkComputerAttempt(lastTile);
                        attempts.add(lastTile);
                    } catch (TileException e) {
                        directionOfNextAttempt++;
                        Log.e("BOAT_IS", "44: Boat is " + lastBoat
                                + ", direction = " + directionOfNextAttempt
                                + " and lastTile = " + lastTile);    // If MISSED, set lastTile back and change direction
                        makeAttempt();
                    }
                    Log.i("BOAT_IS", "22: Boat is " + lastBoat
                            + ", direction = " + directionOfNextAttempt
                            + " and lastTile = " + lastTile);
                    // If MISSED, set lastTile back and change direction
                    // Else, go own from the new successful target
                    if (lastBoat == Boat.SEE) {
                        lastTile = lastTarget;
                        directionOfNextAttempt++;
                    }
                } else {
                    lastTile = lastTarget;
                    directionOfNextAttempt++;
                    Log.i("BOAT_IS", "55: Boat is " + lastBoat
                            + ", direction = " + directionOfNextAttempt
                            + " and lastTile = " + lastTile);
                    makeAttempt();
                }
            }
        } catch (TileException e) {
            e.printStackTrace();
            lastTile = lastTarget;
            makeAttempt();
        }*/
    }

    /**
     * Check if a player's attempt is successful.
     *
     * @param tile the targeted tile
     * @return the Boat reached (or Boat.SEE)
     */
    public TileStatus checkPlayersAttempt(int tile) {
        autoTiles[tile].setTargeted();
        return autoTiles[tile];
    }

    /**
     * For debugging purposes, print the grid
     */
    public void printGrid() {
        int rows = 12;
        int columns = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(autoTiles[j + (i * columns)].getChar()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_MY_TILES", sb.toString());
    }

    /**
     * Launch a new thread.
     * TODO: check if a Thread is efficient
     */
    public void run() {
        autoTiles = new TileStatus[columns * rows];
        Arrays.fill(autoTiles, new TileStatus(Boat.SEE, false));
        setBoatOnDefault();
        printGrid();
    }
}