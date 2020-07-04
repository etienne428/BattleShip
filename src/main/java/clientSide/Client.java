package clientSide;

import Boat.SetOfBoats;
import Utils.CommandException;
import Utils.CommandToClient;
import Utils.Parser;
import clientSide.Grid.GameFrame;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {
    private static Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private final GameFrame gameFrame = new GameFrame(this);
    private final Thread GRAPHIC = new Thread(gameFrame);

    public Client(String address, int port, String name) {
        try {
            socket = new Socket(address, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Listener consoleListener = new Listener(this, outputStream);
            Listener serverListener = new Listener(this, inputStream);

            GRAPHIC.start();

            consoleListener.start();
            serverListener.start();
            System.out.println("Client running");
            gameFrame.launch(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void quit() {
    }

    public void performAction(String s) {
        System.out.println("Client Received " + s);
        try {
            CommandToClient command = CommandToClient.valueOf(Parser.getCommand(s));
            s = s.substring(5);
            switch (command) {
                case START:
                    gameFrame.startGame(s);
                    break;
                case ATEMP:
                    gameFrame.attempt(s);
                    break;
                case LAUNC:
                    gameFrame.resultOfAttempt(s);
                    break;
                case TOCOL:
                    // will announce that boat has been sank
                    System.out.println("You sank the " + SetOfBoats.getNames()[Integer.parseInt(s)]);
                    break;
                case WINNE:
                    // will announce that game is won
                    System.out.println("You have won!");
                    break;
                case PRINT:
                    gameFrame.printWG();
                default:
                    System.out.println("Incorrect command : " + s);
            }

        } catch (CommandException e) {
            System.out.println("Command " + s + " not valid");
            e.printStackTrace();
        }
    }

    public void sendMessage(String s) {
        s = s + "\r\n";
        try {
            outputStream.write(s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
