package serverSide2Player;

import serverSide.ClientControl;
import serverSide.Game;
import serverSide.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server2Player extends Server implements Runnable {
    private final ServerSocket serverSocket;
    private final Game2P game;
    private final int port;
    // Count of connected clients
    static int count = 0;
    // Maximum Number Of Clients
    static final int NOC = 2;

    /**
     * First client is human, second is automatic
     */
    private final ClientControl2Player[] listOfClient = new ClientControl2Player[NOC];

    public Server2Player(int port) throws IOException {
        this.port = port;
        game = new Game2P();
        serverSocket = new ServerSocket(port);
    }

    public int getPort() {
        return port;
    }

    public ClientControl2Player[] getListOfClient() {
        return listOfClient;
    }

    public void run() {
        try {
            System.out.println("Server " + port + " created here!");
            while (true) {
                if (count < NOC) {
                    Socket clientSocket = serverSocket.accept();
                    ClientControl2Player ct = new ClientControl2Player(this, clientSocket, game);
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
        /*ClientControl2Player cc = new ClientControl2Player(this, );
        System.out.println(cc.toString());
        listOfClient[count++] = cc;
        System.out.println(listOfClient[count - 1].toString());
        cc.start();
        System.out.println(cc.toString());*/
        game.ready();
    }

    @Override
    public void sendToOtherClients(String s, ClientControl cc) {
        if (cc == listOfClient[0]) {
            listOfClient[1].sendMessage(s);
        } else {
            listOfClient[0].sendMessage(s);
        }
    }

    public int getNOC() {
        return NOC;
    }

    public void broadcast(String message) {
        for (ClientControl2Player cc :listOfClient) {
            System.out.println(cc.toString());
            cc.sendMessage(message);
        }
    }

    public void sendToOtherClients(String s, boolean toAutomata) {
        //need to find a solution for this one
        for (ClientControl2Player cc: listOfClient) {
            if (true) {//cc != clientControl) {
                cc.sendMessage(s);
            }
        }
    }
}
