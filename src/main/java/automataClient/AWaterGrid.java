package automataClient;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.TileState;
import clientSide.Grid.Tile;

import java.util.Random;

import static Utils.TileState.*;

public class AWaterGrid {
    private final AClient client;

    public static final int NUMBEROFCOLUMNS = 10;
    public static final int NUMBEROFLINES = 12;
    private final int numberOfTiles = NUMBEROFLINES * NUMBEROFCOLUMNS;
    private final AGameFrame gameFrame;
    private final boolean myGrid;
    private ATile lastAttempt;
    private TileState resultOfLastAttempt;

    /**
     * If last attempt was a miss, field = 0. Else increments to 1, 2, 3 and 4 (north, east, south, west).
     */
    private int directionOfNextAttempt = 0;
    private final Random random = new Random();

    /**
     * As a line, 0 is very first, coded in reading order :
     *  A 0   1   2   3   4   5   6   7   8   9
     *  B 10  11  12  13  14 . . .
     *  C .
     *    .
     *    .
     *  L 110 111 112 113 114 115 116 117 118 119
     */
    private final ATile[] grid = new ATile[numberOfTiles];

    public AWaterGrid(AClient client, AGameFrame gameFrame, boolean myGrid) {
        this.client = client;
        this.gameFrame = gameFrame;
        this.myGrid = myGrid;
        SetOfBoats setOfBoat = new SetOfBoats();
        int k = 0;

        char c = 'A';
        for (int i = 0; i < NUMBEROFLINES; i++) {
            for (int j = 0; j < NUMBEROFCOLUMNS; j++) {
                grid[k++] = new ATile(client, this, c, j, myGrid);
            }
            c++;
        }
    }

    /*public void addBoatFromTile(ATile direction) {
        boolean northSouth;
        if (direction.getLine() == nextBoat.getLine()) {
            northSouth = false;
        } else if (direction.getColumn() == nextBoat.getColumn()) {
            northSouth = true;
        } else {
            return;
        }
        try {
            addBoat(nextBoat.getLine(), nextBoat.getColumn(), SetOfBoats.getLengths()[boatToAdd], northSouth);
            boatToAdd++;
            if (boatToAdd == setOfBoat.numberOfBoats()) {
                client.sendMessage("READY");
            }
        } catch (BoatNotSetException e) {
            System.out.println(e.getMessage());
        }
    }*/

    /**
     * Add a boat from clientSide.Grid.Tile (l, c) = (A, 3).
     *
     * @param l           line of the first clientSide.Grid.Tile,  A <= l <= L
     * @param c           column of the first clientSide.Grid.Tile 0 <= c <= 9
     * @param length      length of the boat
     * @param northSouth  orientation of the boat
     */
    public void addBoat(char l, int c, int length, boolean northSouth) throws BoatNotSetException {

        int tilesToJump;
        if (l < 'A' || l > 'L' || c < 0 || c > 9) {
            throw new BoatNotSetException(l + " " + c + " isn't okay 000");
        }
        if (northSouth) {
            tilesToJump = NUMBEROFCOLUMNS;
            if (c + length > 9) {
                throw new BoatNotSetException(c + " " + c+length + " isn't okay 111");
            }
        } else {
            tilesToJump = 1;
            if (l + length > 'L') {
                throw new BoatNotSetException(l + " " + l+length + " isn't okay 222");
            }
        }

        int index = getPointIndex((int) l - 'A', c);
        ATile p =  grid[index];
        for (int i = 0; i < length; i++) {
            p.isEmpty();
            index += tilesToJump;
            //System.out.println(index + " index");
            p = grid[index];
        }

        index = getPointIndex((int) l - 'A', c);
        for (int i = 0; i < length; i++) {
            p = grid[index];
            p.setBoat();
            index += tilesToJump;
        }
        //System.out.println("Boat added!!!!!!!!!!!!!");
    }

    /**
     *
     *
     * Get the index of the Tile in grid []
     *
     * @param line    line of the Tile
     * @param column  column of the tile
     * @return        the index in array grid[]
     */
    public int getPointIndex(int line, int column) {
        if (column >= NUMBEROFCOLUMNS || line >= NUMBEROFLINES) {
            return -1;
        }
        //System.out.println(column + " column, line = " + line);
        return column + (line * (NUMBEROFCOLUMNS));
    }

    public void printBoard() {
        System.out.println("Printing Automata's board, myGrid = " + myGrid);
        System.out.println("__|_0_1_2_3_4_5_6_7_8_9");
        ATile t;
        TileState value;
        char[] letters = {'A','B','C','D','E','F','G','H','I','J','K','L'};
        for (int i = 0; i < NUMBEROFLINES; i++) {
            System.out.print(letters[i] + " |");
            for (int j = 0; j < NUMBEROFCOLUMNS; j++) {
                t = grid[i * NUMBEROFCOLUMNS + j];
                value = t.getValue();
                switch (value) {
                    case SEE:
                        System.out.print("__");
                        break;
                    case BOAT:
                        System.out.print(" B");
                        break;
                    case MISSED:
                        System.out.print(" O");
                        break;
                    case TOUCHED:
                        System.out.print(" X");
                        break;
                    default:
                        assert(value == SEE);
                        System.out.println("   " + value + " " + SEE + "    is not gültig");
                }
            }
            System.out.println();
        }
    }

    /*public void setBeginOfNextBoat(ATile tile) {
        nextBoat = tile;
    }*/

    /**
     *
     *
     * Check attempt from other player.
     *
     * @param s One letter and one int for the grid coordinate
     */
    public void attemptCheck(String s) {
        s = s.replaceAll(" ", "");
        s = s.toUpperCase();
        if (s.length() != 2) {
            System.out.println("Wrong parsing of Tile : " + s);
        }
        int line = ((int) s.charAt(0)) - 65;
        int column = Integer.parseInt(String.valueOf(s.charAt(1)));

        String message = grid[getPointIndex(line, column)].action();
        if (message.contains("X")) {
            gameFrame.resultOfAttemptO("LAUNC TOUCH");
        } else {
            gameFrame.resultOfAttemptO("LAUNC MISS");
        }
    }

    /**
     *
     *
     * Store the result of this automata's last attempt.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttempt(String missOrTouch) {
        //System.out.println(missOrTouch);
        TileState attempt = lastAttempt.resultOfAttempt(missOrTouch);
        if (attempt == MISSED && directionOfNextAttempt != 0) {
            directionOfNextAttempt %= 4;
            directionOfNextAttempt++;
        } else if (attempt == TOUCHED){
            resultOfLastAttempt = attempt;
            if (directionOfNextAttempt == 0) {
                directionOfNextAttempt++;
            }
        } else {
            resultOfLastAttempt = attempt;
        }
    }

    /**
     *
     *
     * Randomly (for now) choose a Tile to target.
     *
     * @return  One letter and one int for the grid coordinate
     */
    public String getNextAttempt() {
        if (lastAttempt != null) {
            System.out.println("directionOfNextAttempt " + directionOfNextAttempt + ", lastTile = " + lastAttempt.getLine() + " " + lastAttempt.getColumn());
        }
        if (directionOfNextAttempt != 0) {
            char line = lastAttempt.getLine();
            int column = lastAttempt.getColumn();
            switch (directionOfNextAttempt) {
                case 1:
                    line--;
                    break;
                case 2:
                    column++;
                    break;
                case 3:
                    line++;
                    break;
                case 4:
                    column--;
                    break;
                default:
                    System.out.println("Error : directionOfNextAttempt = " + directionOfNextAttempt);
                    return getNextAttempt();
            }
            if (checkForNextAttempt(line, column)) {
                return line + "" + column;
            } else if (directionOfNextAttempt == 4) {
                directionOfNextAttempt = 0;
                return getNextAttempt();
            } else {
                directionOfNextAttempt++;
                return getNextAttempt();
            }
        }
        char line = (char) (random.nextInt(12) + 65);
        int column = random.nextInt(10);
        if (checkForNextAttempt(line, column)) {
            lastAttempt = grid[getPointIndex(line - 65, column)];
            return line + "" + column;
        } else {
            return getNextAttempt();
        }
    }

    /**
     * Check that a tile is valid and hasn't been targeted yet
     *
     * @return True if Tile is a good choice
     */
    private boolean checkForNextAttempt(char line, int column) {
        int index;
        try {
            index = getPointIndex(line - 65, column);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }

        if (index < 0) {
            return false;
        }
        return grid[index].getValue() == SEE;
    }
}
