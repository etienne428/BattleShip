package automataClient;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import serverSide.Game;

import java.util.Random;

public class AGameFrame implements Runnable {

    private final AClient client;
    private final Game game;
    private AWaterGrid myGrid;
    private AWaterGrid opponentGrid;
    private final Random random = new Random();

    public AGameFrame(AClient client, Game game) {
        this.client = client;
        this.game = game;
    }

    public void launch() {
        myGrid = new AWaterGrid(client, this, true);
        setBoatOnDefault();
        game.setAutomataBoats(myGrid.getBoatsPlacement());
        game.setNextAttempt(myGrid.getNextAttempt());
    }

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
                //System.out.println(e.getMessage());

            }
        }
        myGrid.printBoard();
        game.setAutomataBoats(myGrid.getBoatsPlacement());
    }

    /**
     * Check attempt from other player.
     *
     * @param s One letter and one int for the grid coordinate
     */
    public void attemptCheck(String s) {
        myGrid.attemptCheck(s);
    }

    @Override
    public void run() {

    }

    public void startGame(String message) {
        myGrid.startGame();

        opponentGrid = new AWaterGrid(client, this, false);
        String[] tiles = message.split(" ");

        for (int i = 0; i < SetOfBoats.getLengths().length; i++) {
            String t = tiles[i];
            try {
                if (t.length() != 3) {
                    throw new BoatNotSetException(t + " is not a proper tile encoding");
                }
                opponentGrid.addBoat( t.charAt(0), t.charAt(1), SetOfBoats.getLengths()[i++],
                        t.substring(2).equalsIgnoreCase("N"));
            } catch (BoatNotSetException e) {
                System.out.println(i-- + " has to be set better");
                //e.printStackTrace();
            }
        }
    }

    /**
     * Send the result of the attempt back.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttempt(String missOrTouch) {
        opponentGrid.resultOfAttempt(missOrTouch);
    }

    /**
     * Sends the result of the other player's attempt to game.java.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttemptO(String missOrTouch) {
        game.setResultOfAttemptH(missOrTouch);
    }
}