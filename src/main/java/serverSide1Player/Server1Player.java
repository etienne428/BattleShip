package serverSide1Player;

import serverSide.ClientControl;
import serverSide.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1Player extends Server implements Runnable {
    private ServerSocket serverSocket = null;
    private final Game1P game;
    private final int port;

    private ClientControl player;



    public Server1Player(int port) throws IOException {
        this.port = port;
        game = new Game1P(this);
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        try {
            System.out.println("Server " + port + " created here!");
            Socket clientSocket = serverSocket.accept();
            player = new ClientControl1Player(this, clientSocket, game);
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }

    public void ready(ClientControl clientControl) {
        game.ready();
    }

    @Override
    public void sendToOtherClients(String s, ClientControl cc) {
        broadcast(s);
        System.out.println("Please do not use this method.");
    }

    public void broadcast(String message) {
        player.sendMessage(message);
    }

    @Override
    public int getNOC() {
        return 2;
    }

    public void sendToOtherClients(String s, boolean toAutomata) {
        if (toAutomata) {

        } else {
            player.sendMessage(s);
        }
    }
}
