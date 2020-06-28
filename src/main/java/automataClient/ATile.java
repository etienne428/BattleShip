package automataClient;

import Utils.BoatNotSetException;
import Utils.TileState;

import javax.swing.*;

public class ATile extends JButton {
    private static final int SETBOATFIRST = 0;
    private static final int SETBOATSECOND = 1;
    private static final int PLAY = 2;

    private static int gameStatus;

    private final AClient client;
    private final AWaterGrid wg;
    private final char line;
    private final int column;
    private TileState value = TileState.SEE;

    public ATile(AClient client, AWaterGrid wg, char line, int column, boolean myGrid) {

        this.client = client;
        this.wg = wg;
        this.line = line;
        this.column = column;
        gameStatus = SETBOATFIRST;
        //setMinimumSize(new Dimension(80, 40));
        if (!myGrid && value == TileState.BOAT) {
            setText(String.valueOf(TileState.SEE));
        } else {
            setText(String.valueOf(value.getDescription()));
        }
        addActionListener(e -> attempt());
    }

    public static void setPlaying() {
        gameStatus = PLAY;
    }

    private void attempt() {
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
    }

    public void setBoat() throws BoatNotSetException {
        if (value == TileState.BOAT) {
            throw new BoatNotSetException("Tile is already occupied");
        }
        value = TileState.BOAT;
        setText(String.valueOf(value.getDescription()));
    }

    public TileState getValue() {
        return value;
    }

    public String action() {
        if (value == TileState.BOAT) {
            value = TileState.TOUCHED;
        } else if (value == TileState.SEE) {
            value = TileState.MISSED;
        }
        setText(String.valueOf(value.getDescription()));
        System.out.println(value.name());
        return String.valueOf(value.getDescription());
    }

    public void isEmpty() throws BoatNotSetException {
        if (value == TileState.BOAT) {
            throw new BoatNotSetException("Tile is already occupied");
        }
    }

    public char getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public void resultOfAttempt(String s) {
        if (s.equalsIgnoreCase("MISS ")) {
            value = TileState.MISSED;
        } else if (s.equalsIgnoreCase("TOUCH ")) {
            value = TileState.TOUCHED;
        } else {
            System.out.println(s + " is not correct, should be MISS or TOUCH ");
        }
    }
}