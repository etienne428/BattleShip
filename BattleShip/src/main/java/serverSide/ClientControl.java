package serverSide;

import Utils.CommandException;
import Utils.CommandToServer;
import Utils.Parser;
import serverSide.Game.Game;

import java.io.*;
import java.net.Socket;

public class ClientControl {

    private String name;
    private Socket socket = null;
    private Server server = null;
    private OutputStream outputStream;
    private Game game;


    public ClientControl() {

    }

    public ClientControl(Server server, Socket clientSocket, Game game) {
        this.server = server;
        name = "Player";
        this.socket = clientSocket;
        this.game = game;
    }

    public void start() {
        System.out.println("Start client " + server.getPort());
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

    private void executeCommand(String s) {
        System.out.println("Server received " + s);
        try {
            CommandToServer command = CommandToServer.valueOf(Parser.getCommand(s));
            s = s.substring(5);
            switch (command) {
                case READY:
                    server.ready(this);
                    break;
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
        message += "\r\n";
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
