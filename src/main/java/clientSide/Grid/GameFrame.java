package clientSide.Grid;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import clientSide.Client;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameFrame extends JFrame implements Runnable {

    private final Client client;
    private String name;
    private WaterGrid myGrid;
    private WaterGrid opponentGrid;
    private JTextArea text;
    private final Random random = new Random();

    public GameFrame(Client client) {
        this.client = client;
    }

    public void launch(String name) {
        this.name = name;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationByPlatform(true);
        setLayout(new GridLayout(2, 1));
        setMinimumSize(new Dimension(300, 300));

        myGrid = new WaterGrid(client, this, true);
        add(myGrid);
        pack();
        setVisible(true);
        setBoatOnDefault();
    }

    void setBoatOnDefault() {
        opponentGrid = new WaterGrid(client, this, false);
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
                myGrid.addBoat((char) line, column, SetOfBoats.getLengths()[i], northSouth);
            } catch (BoatNotSetException e) {
                //System.out.println(e.getMessage());
                i--;
                //System.out.println("Boat not set.");

            }
        }
        myGrid.printBoard();
        setStartView();
    }

    private void setStartView() {
        remove(myGrid);
        myGrid.startGame();
        opponentGrid.setMaximumSize(new Dimension(200, 200));

        add(opponentGrid);
        JPanel myPanel = new JPanel();
        myPanel.setLayout(new BorderLayout());
        myPanel.setMaximumSize(new Dimension(400, 200));
        myGrid.setMaximumSize(new Dimension(200, 200));
        myPanel.add(myGrid, BorderLayout.LINE_START);

        text = new JTextArea();
        text.setMaximumSize(new Dimension(100, 200));
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBar(new JScrollBar());
        scrollPane.setMaximumSize(new Dimension(100, 200));
        //text.setFont(Font.getFont(""));
        text.append("Choose a tile to target");
        myPanel.add(text, BorderLayout.CENTER);
        add(myPanel);
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
                    throw new BoatNotSetException(t + " is not a proper tile encoding");
                }
                opponentGrid.addBoat( t.charAt(0), t.charAt(1), SetOfBoats.getLengths()[i++],
                        t.substring(2).equalsIgnoreCase("N"));
            } catch (BoatNotSetException e) {
                e.printStackTrace();
            }
        }
        setStartView();
    }

    public void resultOfAttempt(String s) {
        opponentGrid.resultOfAttempt(s);
        repaint();
        revalidate();
        pack();
    }

    public void printWG() {
        myGrid.printBoard();
        if (opponentGrid != null) {
            opponentGrid.printBoard();
        }
    }

    public void setText(String t) {
        text.replaceRange(t, 0, text.getSelectionEnd());
    }
}
