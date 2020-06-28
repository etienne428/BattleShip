package clientSide.Grid;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.Parser;
import clientSide.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameFrame extends JFrame implements Runnable {

    private final Client client;
    private String name;
    private WaterGrid myGrid;
    private WaterGrid opponentGrid;
    private final Random random = new Random();

    public GameFrame(Client client) {
        this.client = client;
    }

    public void launch(String name) {
        this.name = name;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(2, 1));
        setMinimumSize(new Dimension(500, 500));

        myGrid = new WaterGrid(client, this, true);
        add(myGrid);
        pack();
        setVisible(true);
    }

    void setBoatOnDefault() {
        for (int i = 0; i < 5; i++) {
            boolean northSouth = random.nextBoolean();
            int line;
            int column;
            if (northSouth) {
                line = random.nextInt(12 - SetOfBoats.lengths[i]) + 65;
                column = random.nextInt(10);
            } else {
                line = random.nextInt(12) + 65;
                column = random.nextInt(10 - SetOfBoats.lengths[i]);

            }
            try {
                System.out.println("north " + northSouth + ", line " + (char) line + ", column " + column);
                myGrid.addBoat((char) line, column, SetOfBoats.lengths[i], northSouth);
            } catch (BoatNotSetException e) {
                e.printStackTrace();
                System.out.println("Boat not set.");

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

        opponentGrid = new WaterGrid(client, this, false);
        String[] tiles = message.split(" ");
        int i = 0;
        for (String t: tiles) {
            try {
                if (t.length() != 3) {
                    System.out.println(t + " is not a proper tile encoding");
                    throw new BoatNotSetException();
                }
                opponentGrid.addBoat( t.charAt(0), t.charAt(1), SetOfBoats.lengths[i++],
                        t.substring(2).equalsIgnoreCase("N"));
            } catch (BoatNotSetException e) {
                e.printStackTrace();
            }
        }
        remove(myGrid);
        add(opponentGrid);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
    }
}
