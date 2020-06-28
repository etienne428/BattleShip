import clientSide.Client;
import serverSide.Game.Game;
import serverSide.Server;

import java.io.IOException;
import java.net.BindException;

public class BSMain {
    private static Server server;

    private static boolean setServer(int port) {
        try {
            server = new Server(port);
        } catch (BindException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {
        int port;
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 42824;
        }
        if (setServer(port)) {
            Thread serverThread = new Thread(server);
            serverThread.start();
        }
        try {
            //server.runServer();

            System.out.println("Jetzt isch los");
            new Client("localhost",port, "");
            //Game game = new Game();
            //game.run();
        } catch (Exception e) {
            System.out.println("Couldn't start game : ");
            e.printStackTrace();
        }

    }
}
