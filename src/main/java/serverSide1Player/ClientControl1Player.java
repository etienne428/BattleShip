package serverSide1Player;

import Utils.CommandException;
import Utils.CommandToServer;
import Utils.Parser;
import automataClient.AClient;
import serverSide.ClientControl;
import serverSide.Server;

import java.io.*;
import java.net.Socket;

public class ClientControl1Player extends ClientControl {


    private final String name;
    private final Socket socket;
    private final Server server;
    private OutputStream outputStream;
    private final Game1P game;

    public ClientControl1Player(Server server, Socket clientSocket, Game1P game) {
        this.server = server;
        name = "Player";
        this.socket = clientSocket;
        this.game = game;
        game.addPlayer(this);
    }

    public void start() {
        //tem.out.println("Start client " + server.getPort());

        String command;
        try {
            InputStream inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            // add name
            // add Client to server.getClientList
            while (true) {
                command = reader.readLine();
                if (command == null) {
                    continue;
                }
                //System.out.println("Received " + command);
                executeCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String s) {
        //System.out.println("Server received " + s);
        try {
            CommandToServer command = CommandToServer.valueOf(Parser.getCommand(s));
            s = Parser.getElement(s);

            switch (command) {

                case READY:
                    game.ready();
                    break;

                case ATEMP:
                    // Check automata's attempt (Tile coo. is stored in game.java)
                    game.attemptA();
                    // Check human's player's attempt
                    game.attemptH(s);
                    break;

                case LAUNC:
                    // Result of an attempt : MISS or TOUCH
                    game.sendResultToA(s);
                    break;

                case TOCOL:
                    // Automata sank a boat from human's set. Do nothing.
                    break;

                case LOSTG:
                    server.broadcast("WINNE " + this.name);
                    break;

                case PRINT:
                    //server.broadcast("PRINT ");
                    game.printAGrid();
                    break;

            }

        } catch (CommandException e) {
            System.out.println("Command " + s + " not valid");
            e.printStackTrace();
        }    }

    public void sendMessage(String message) {
        message += "\r\n";
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
