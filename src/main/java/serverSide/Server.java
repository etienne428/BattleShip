package serverSide;

import serverSide.Game.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread implements Runnable {
    private final ServerSocket serverSocket;
    private Game game;
    private final int port;
    // Count of connected clients
    static int count = 0;
    // Maximum Number Of Clients
    static final int NOC = 2;

    /**
     * First client is human, second is automatic
     */
    private final ClientControl[] listOfClient = new ClientControl[NOC];

    public Server(int port) throws IOException {
        this.port = port;
        game = new Game(this);
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
            System.out.println("Server " + port + " created here!");
            while (true) {
                if (count < NOC) {
                    Socket clientSocket = serverSocket.accept();
                    ClientControl ct = new ClientControl(this, clientSocket, game);
                    listOfClient[count++] = ct;
                    ct.start();
                    System.out.println("I'm here");
                    count = 2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ready(ClientControl clientControl) {
        ClientControl cc = new ClientControl(this);
        System.out.println(cc.toString());
        listOfClient[count++] = cc;
        System.out.println(listOfClient[count - 1].toString());
        cc.start();
        System.out.println(cc.toString());
        game.ready();
    }

    public int getNOC() {
        return NOC;
    }

    public void broadcast(String message) {
        for (ClientControl cc :listOfClient) {
            System.out.println(cc.toString());
            cc.sendMessage(message);
        }
    }

    public void sendToOtherClients(String s, ClientControl clientControl) {
        for (ClientControl cc: listOfClient) {
            if (cc != clientControl) {
                cc.sendMessage(s);
            }
        }
    }
}
