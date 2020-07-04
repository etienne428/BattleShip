package serverSideMP;

import Utils.CommandException;
import Utils.CommandToServer;
import Utils.Parser;
import automataClient.AClient;
import serverSide.ClientControl;
import serverSide.Server;

import java.io.*;
import java.net.Socket;

public class ClientControlMP extends ClientControl {


    private AClient autoClient = null;
    private final boolean automata;
    private String name;
    private Socket socket = null;
    private Server server = null;
    private OutputStream outputStream;
    private GameMP game;

    /**
     * Constructor for Automata Client.
     *
     * @param server the server
     */
    public ClientControlMP(Server server) {
        automata = true;

        this.server = server;
        autoClient = new AClient(game);
    }

    public ClientControlMP(Server server, Socket clientSocket, GameMP game) {
        automata = false;
        this.server = server;
        name = "Player";
        this.socket = clientSocket;
        this.game = game;
    }

    public void start() {
        System.out.println("Start client " + server.getPort());
        if (automata) {
            return;
        }
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
                System.out.println("Received " + command);
                executeCommand(command);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void executeCommand(String s) {
        System.out.println("Server received " + s);
        try {
            CommandToServer command = CommandToServer.valueOf(Parser.getCommand(s));
            s = Parser.getElement(s);
            switch (command) {
                case READY:
                    server.ready(this);
                    break;
                case ATEMP:
                    //game.attempt(s);
                    server.sendToOtherClients("ATEMP " + s, this);
                    break;
                case LAUNC:
                    server.sendToOtherClients("LAUNC " + s, this);
                    break;
                case TOCOL:
                    server.sendToOtherClients("TOCOL ", this);
                    break;
                case LOSTG:
                    server.broadcast("WINNE " + this.name);
                case PRINT:
                    server.sendToOtherClients("PRINT ", this);
                    break;

            }

        } catch (CommandException e) {
            System.out.println("Command " + s + " not valid");
            e.printStackTrace();
        }    }

    public void sendMessage(String message) {
        if (automata) {
            autoClient.performAction(message);
        } else {
            message += "\r\n";
            try {
                outputStream.write(message.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
