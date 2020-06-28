package serverSide;

import serverSide.Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread implements Runnable {
    private final ServerSocket serverSocket;
    private final int port;
    // Count of connected clients
    static int count = 0;
    // Maximum Number Of Clients
    static final int NOC = 2;

    /**
     * First client is human, second is automatic
     */
    private final ClientControl[] listOfClient = new ClientControl[NOC];
    private Game game;

    public Server(int port) throws IOException {
        this.port = port;
        serverSocket = new ServerSocket(port);
    }

    public int getPort() {
        return port;
    }

    public ClientControl[] getListOfClient() {
        return listOfClient;
    }

    public void run() {
        try {
            System.out.println("Server " + port + " created");
            while (true) {
                if (count < NOC) {
                    Socket clientSocket = serverSocket.accept();
                    ClientControl ct = new ClientControl(this, clientSocket, game);
                    ct.start();
                    listOfClient[count++] = ct;
                    System.out.println("I'm here");
                    listOfClient[1] = new AutoClient(this, clientSocket, game);
                    count++;
                    listOfClient[1].start();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ready(ClientControl clientControl) {
        game.ready();
    }

    public int getNOC() {
        return NOC;
    }

    public void broadcast(String message) {
        for (ClientControl cc :listOfClient) {
            cc.sendMessage(message);
        }
    }

    public void sendToOtherClients(String s, ClientControl clientControl) {
        for (ClientControl cc: listOfClient) {
            if (cc != clientControl) {
                cc.sendMessage("ATEMP " + s);
            }
        }
    }
}
