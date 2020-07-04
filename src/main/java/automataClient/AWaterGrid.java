package automataClient;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.TileState;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static Utils.TileState.*;

public class AWaterGrid extends JPanel {
    private final AClient client;

    public static final int NUMBEROFCOLUMNS = 10;
    public static final int NUMBEROFLINES = 12;
    private final int numberOfTiles = NUMBEROFLINES * NUMBEROFCOLUMNS;
    private final AGameFrame gameFrame;
    private final boolean myGrid;
    private ATile nextBoat;
    private ATile lastAttempt;
    private final SetOfBoats setOfBoat;
    private int boatToAdd = 0;
    private String boatsPlacement = "";
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
                grid[k] = new ATile(client, this, c, j, myGrid);
                add(grid[k++]);
            }
            c++;
        }
    }

    public void addBoatFromTile(ATile direction) {
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
    }

    public void startGame() {
        ATile.setPlaying();
        for (ATile t: grid) {
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
        if (northSouth) {
            boatsPlacement += l + c + "S";
        } else {
            boatsPlacement += l + c + "E";
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

    public void setBeginOfNextBoat(ATile tile) {
        nextBoat = tile;
    }

    /**
     * Check attempt from other player.
     *
     * @param s One letter and one int for the grid coordinate
     */
    public void attemptCheck(String s) {
        s = s.replace(" ", "");
        s = s.toUpperCase();
        if (s.length() != 2) {
            System.out.println("Wrong parsing of Tile : " + s);
        }
        s = s.replaceAll(" ", "");
        System.out.println(s + " should be a letter and a number without space");
        int line = ((int) s.charAt(0)) - 65;
        int column = Integer.parseInt(String.valueOf(s.charAt(1)));
        //System.out.println(s + " should be a letter and a number without space. it gives line "
          //      + line + " + column " + column + " " + s.charAt(1));
        String message = grid[getPointIndex(line, column)].action();
        if (message.contains("X")) {
            gameFrame.resultOfAttemptO("LAUNC TOUCH");
        } else {
            client.sendMessage("LAUNC MISS");
        }
    }

    /**
     * Send the result of the attempt back.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttempt(String missOrTouch) {
        lastAttempt.resultOfAttempt(missOrTouch);
    }

    public void setAttempt(ATile tile) {
        lastAttempt = tile;
    }

    public String getBoatsPlacement() {
        return boatsPlacement;
    }

    public String getNextAttempt() {
        char line = (char) (random.nextInt(12) + 65);
        int column = random.nextInt(10);
        return line + "" + column;
    }
}