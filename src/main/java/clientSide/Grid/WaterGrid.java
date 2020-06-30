package clientSide.Grid;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.TileState;
import clientSide.Client;

import javax.swing.*;
import java.awt.*;

import static Utils.TileState.*;

public class WaterGrid extends JPanel {
    public static final int SETBOATFIRST = 0;
    public static final int SETBOATSECOND = 1;
    public static final int PLAY = 2;
    private static int gameStatus;

    private final Client client;

    public static final int NUMBEROFCOLUMNS = 10;
    public static final int NUMBEROFLINES = 12;
    private final int numberOfTiles = NUMBEROFLINES * NUMBEROFCOLUMNS;
    private final GameFrame game;
    private final boolean myGrid;
    private Tile nextBoat;
    private Tile lastAttempt;
    private final SetOfBoats setOfBoat;
    private int boatToAdd = 0;

    /**
     * As a line, 0 is very first, coded in reading order :
     *  A 0   1   2   3   4   5   6   7   8   9
     *  B 10  11  12  13  14 . . .
     *  C .
     *    .
     *    .
     *  L 110 111 112 113 114 115 116 117 118 119
     */
    private final Tile[] grid = new Tile[numberOfTiles];

    public WaterGrid(Client client, GameFrame game, boolean myGrid) {
        if (myGrid) {
            gameStatus = SETBOATFIRST;
        } else {
            gameStatus = PLAY;
        }
        this.client = client;
        this.game = game;
        this.myGrid = myGrid;
        setOfBoat = new SetOfBoats();
        int k = 0;
        setLayout(new GridLayout(NUMBEROFLINES + 1, NUMBEROFCOLUMNS + 1));
        JLabel all = new JLabel("");
        add(all);
        all.setAlignmentX(CENTER_ALIGNMENT);
        for (int i = 1; i <= 10; i++) {
            all = new JLabel(String.valueOf(i), SwingConstants.CENTER);
            all.setAlignmentX(CENTER_ALIGNMENT);
            add(all);
        }
        char c = 'A';
        for (int i = 0; i < NUMBEROFLINES; i++) {
            all = new JLabel(String.valueOf(c), SwingConstants.CENTER);
            all.setAlignmentX(CENTER_ALIGNMENT);
            add(all);
            for (int j = 0; j < NUMBEROFCOLUMNS; j++) {
                grid[k] = new Tile(client, this, c, j, myGrid);
                add(grid[k++]);
            }
            c++;
        }
    }

    public void addBoatFromTile(Tile direction) {
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
                gameStatus = PLAY;
                client.sendMessage("READY");
            }
        } catch (BoatNotSetException e) {
            System.out.println(e.getMessage());
        }
        gameStatus = SETBOATFIRST;
    }

    public void startGame() {
        gameStatus = PLAY;
        for (Tile t: grid) {
            t.setEnabled(false);
        }
    }

    /**
     * Add a boat from clientSide.Grid.Tile (l, c) = (A, 3).
     *
     * @param l           line of the first clientSide.Grid.Tile,  A <= l <= L
     * @param c           column of the first clientSide.Grid.Tile 0 <= c <= 9
     * @param length      length of the boat
     * @param northSouth  orientation of the boat
     */
    public void addBoat(char l, int c, int length, boolean northSouth) throws BoatNotSetException {

        //c--;
        int tilesToJump;
        if (l < 'A' || l > 'L' || c < 0 || c > 9) {
            throw new BoatNotSetException(l + " " + c + " isn't okay 00");
        }
        if (northSouth) {
            tilesToJump = NUMBEROFCOLUMNS;
            if (l + length - 1 > 'L') {
                throw new BoatNotSetException(l + " " + (char) ((int) l+length) + " isn't okay 11");
            }
        } else {
            tilesToJump = 1;
            if (c + length - 1 > 9) {
                gameStatus = SETBOATFIRST;
                throw new BoatNotSetException(c + " " + c+length + " isn't okay 22");
            }
        }

        int index = getPointIndex((int) l - 'A', c);
        Tile p =  grid[index];
        for (int i = 0; i < length - 1; i++) {
            p.isEmpty();
            System.out.println("Index = " + index);
            index += tilesToJump;
            if (index >= numberOfTiles) {
                System.out.println(p.getLine() + " is equal to " + (p.getLine() - 65));
                throw new BoatNotSetException("Place not valid " + index + " " + p.getLine() + p.getColumn());
            }
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

    public int getPointIndex(int line, int column) {
        if (column >= NUMBEROFCOLUMNS || line >= NUMBEROFLINES) {
            return -1;
        }
        //System.out.println(column + " column, line = " + line);
        return column + (line * (NUMBEROFCOLUMNS));
    }

    public void printBoard() {
        System.out.println("__|_0_1_2_3_4_5_6_7_8_9");
        Tile t;
        TileState value;
        char[] letters = {'A','B','C','D','E','F','G','H','I','J','K','L'};
        for (int i = 0; i < NUMBEROFLINES; i++) {
            System.out.print(letters[i] + " |");
            for (int j = 0; j < NUMBEROFCOLUMNS; j++) {
                t = grid[i * NUMBEROFCOLUMNS + j];
                value = t.getValue();
                switch (value) {
                    case SEE:
                        System.out.print(" _");
                        break;
                    case BOAT:
                        if (myGrid) {
                            System.out.print(" B");
                        } else {
                            System.out.println(" S");
                        }
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

    public void setBeginOfNextBoat(Tile tile) {
        gameStatus = SETBOATSECOND;
        nextBoat = tile;
    }

    public void attempt(String s) {
        s = s.replace(" ", "");
        s = s.toUpperCase();
        if (s.length() != 2) {
            System.out.println("Wrong parsing of Tile : " + s);
        }
        System.out.println(s + " should be a letter and a number without space");
        int line = ((int) s.charAt(0)) - 65;
        int column = s.charAt(1);
        String message = grid[getPointIndex(line, column)].action();
        client.sendMessage("LAUNC " + message);
    }

    public void resultOfAttempt(String s) {
        lastAttempt.resultOfAttempt(s);
    }

    public void setAttempt(Tile tile) {
        lastAttempt = tile;
    }

    public int getGameStatus() {
        return gameStatus;
    }
}
