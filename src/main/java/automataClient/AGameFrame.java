package automataClient;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import serverSide.Game;

import java.util.Random;

public class AGameFrame {

    private final AClient client;
    private final Game game;
    private final AWaterGrid myGrid;
    private AWaterGrid opponentGrid;
    private final Random random = new Random();

    /**
     * The equivalent of GameFrame, interface between AClient and AWaterGrid.
     *
     * @param client the related Client
     * @param game   the server part, used as interface
     */
    public AGameFrame(AClient client, Game game) {
        this.client = client;
        this.game = game;
        myGrid = new AWaterGrid(client, this, true);
        setBoatOnDefault();
    }

    /**
     *
     */
    void setBoatOnDefault() {
        for (int i = 0; i < 5; i++) {
            boolean northSouth = random.nextBoolean();
            int line;
            int column;
            if (northSouth) {
                line = random.nextInt(12 - SetOfBoats.getLengths()[i]) + 65;
                column = random.nextInt(10);
            } else {
                line = random.nextInt(12) + 65;
                column = random.nextInt(10 - SetOfBoats.getLengths()[i]);

            }
            try {
                //System.out.println("north " + northSouth + ", line " + (char) line + ", column " + column);
                myGrid.addBoat((char) line, column, SetOfBoats.getLengths()[i], northSouth);
            } catch (BoatNotSetException e) {
                i--;
                //System.out.println(e.getMessage());

            }
        }
        //myGrid.printBoard();
    }

    /**
     *
     *
     * Check attempt from other player.
     *
     * @param s One letter and one int for the grid coordinate
     */
    public void attemptCheck(String s) {
        myGrid.attemptCheck(s);
    }

    /**
     *
     *
     * Start game, initiate opponentGrid and send a first attempt.
     */
    public void startGame() {
        opponentGrid = new AWaterGrid(client, this, false);
        game.setNextAttempt(myGrid.getNextAttempt());
    }

    /**
     *
     *
     * Store the result of this automata back.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttempt(String missOrTouch) {
        myGrid.resultOfAttempt(missOrTouch);
    }

    /**
     *
     *
     * Sends the result of the other player's attempt to game.java.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttemptO(String missOrTouch) {
        game.sendResultToH(missOrTouch);
        game.setNextAttempt(myGrid.getNextAttempt());
    }

    public void printGrid() {
        myGrid.printBoard();
        opponentGrid.printBoard();
    }
}