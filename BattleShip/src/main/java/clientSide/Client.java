package clientSide;

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

    public Client(String address, int port, String name) throws IOException {
        try {
            socket = new Socket(address, port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            Listener consoleListener = new Listener(this, outputStream);
            Listener serverListener = new Listener(this, inputStream, outputStream);

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
                case ATEMP:
                    gameFrame.attempt(s);
                    break;
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
