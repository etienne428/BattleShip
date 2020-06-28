package automataClient;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.Parser;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class AGameFrame implements Runnable {

    private final AClient client;
    private String name;
    private AWaterGrid myGrid;
    private AWaterGrid opponentGrid;
    private final Random random = new Random();

    public AGameFrame(AClient client) {
        this.client = client;
    }

    public void launch() {
        myGrid = new AWaterGrid(client, this, true);
        setBoatOnDefault();
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
                System.out.println("north " + northSouth + ", line " + (char) line + ", column " + column);
                myGrid.addBoat((char) line, column, SetOfBoats.getLengths()[i], northSouth);
            } catch (BoatNotSetException e) {
                System.out.println(e.getMessage());

            }
        }
        myGrid.printBoard();
    }

    public void attempt(String s) {
        myGrid.attempt(s);
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

    public void resultOfAttempt(String s) {
        opponentGrid.resultOfAttempt(s);
    }
}