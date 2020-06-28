package serverSide;

import Boat.SetOfBoats;
import Utils.BoatNotSetException;
import Utils.CommandException;
import Utils.CommandToServer;
import Utils.Parser;
import clientSide.Grid.WaterGrid;
import serverSide.Game.Game;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;

public class AutoClient extends ClientControl {
    private final Socket socket;
    private final Game game;
    private String name;
    private final Server server;
    private final WaterGrid wg;

    private final Random random = new Random();

    public AutoClient(Server server, Socket clientSocket, Game game) {
        super();
        this.server = server;
        name = "Player2";
        this.socket = clientSocket;
        this.game = game;
        wg = new WaterGrid(null);
        setBoats();
    }

    private void setBoats() {
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
                wg.addBoat((char) line, column, SetOfBoats.lengths[i], northSouth);
            } catch (BoatNotSetException e) {
                e.printStackTrace();
                System.out.println("Boat not set.");

            }
        }
        wg.printBoard();
    }


    private void executeCommand(String s) {
        System.out.println("Auto received " + s);
        try {
            CommandToServer command = CommandToServer.valueOf(Parser.getCommand(s));
            s = s.substring(5);
            switch (command) {

                case ATEMP:
                    server.sendToOtherClients(s, this);
                    break;
                case LAUNC:


            }

        } catch (CommandException e) {
            System.out.println("Command " + s + " not valid");
            e.printStackTrace();
        }    }


    public void sendMessage(String message) {

    }
}
