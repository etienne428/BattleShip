package automataClient;

import Utils.BoatNotSetException;
import Utils.TileState;

import static Utils.TileState.*;


public class ATile {
    private static final int SETBOATFIRST = 0;
    private static final int SETBOATSECOND = 1;
    private static final int PLAY = 2;

    private static int gameStatus;

    private final AClient client;
    private final AWaterGrid wg;
    private final char line;
    private final int column;
    private TileState value = SEE;

    public ATile(AClient client, AWaterGrid wg, char line, int column, boolean myGrid) {

        this.client = client;
        this.wg = wg;
        this.line = line;
        this.column = column;
        gameStatus = SETBOATFIRST;
    }

    /*private void attempt() {
        switch (gameStatus) {
            case SETBOATFIRST:
                wg.setBeginOfNextBoat(this);
                System.out.println("Boat set from " + line + column);
                gameStatus = SETBOATSECOND;
                break;
            case SETBOATSECOND:
                wg.addBoatFromTile(this);
                gameStatus = SETBOATFIRST;
                break;
            case PLAY:
                client.sendMessage("ATEMP " + line + column);
                wg.setAttempt(this);
                break;
            default :
                System.out.println("error");

        }
    }*/

    /**
     *
     *
     * Set boat on this Tile.
     *
     * @throws BoatNotSetException shouldn't happen as empty() has been called before
     */
    public void setBoat() throws BoatNotSetException {
        if (value == BOAT) {
            throw new BoatNotSetException("Tile is already occupied");
        }
        value = BOAT;
    }

    /**
     *
     *
     * Return the value of the Tile.
     *
     * @return instance of TileState.java
     */
    public TileState getValue() {
        return value;
    }

    /**
     *
     *
     * Set the action as this Tile has been targeted.
     *
     * @return 'X' or 'O' if attempt is a TOUCH or a MISS
     */
    public String action() {
        if (value == BOAT) {
            value = TOUCHED;
        } else if (value == SEE || value ==  MISSED) {
            value = MISSED;
        } else {
            System.out.println("Value was ." + value + ". !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        return String.valueOf(value.getDescription());
    }

    /**
     *
     *
     * Check that Tile is not already occupied by a boat.
     *
     * @throws BoatNotSetException if Tile is occupied, prevent from setting 2 boat on the same Tile
     */
    public void isEmpty() throws BoatNotSetException {
        if (value == BOAT) {
            throw new BoatNotSetException("Tile is already occupied");
        }
    }

    /**
     *
     *
     * Return this tile's line in the grid.
     *
     * @return char between 'A' and 'A'+NUMBEROFLINES
     */
    public char getLine() {
        return line;
    }

    /**
     *
     *
     * Return this tile's column in the grid.
     *
     * @return int between 0 and NUMBEROFCOLUMNS
     */
    public int getColumn() {
        return column;
    }

    /**
     *
     *
     * Store the result of this automata's last attempt.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public TileState resultOfAttempt(String missOrTouch) {
        if (missOrTouch.equalsIgnoreCase("MISS")) {
            value = MISSED;
            return MISSED;
        } else if (missOrTouch.equalsIgnoreCase("TOUCH")) {
            value = TOUCHED;
            return TOUCHED;
        } else {
            System.out.println("\"" + missOrTouch + "\" is not correct, should be MISS or TOUCH ");
            return BOAT;
        }
    }
}