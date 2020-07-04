import clientSide.Client;
import serverSide.Server;
import serverSide1Player.Server1Player;
import serverSide2Player.Server2Player;

import java.io.IOException;
import java.net.BindException;

public class BSMain {
    private static Server server;

    private static boolean setServer(int port) {
        try {
            server = new Server2Player(port);
        } catch (BindException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) {

        // default 1 just for now
        int numberOfPlayer = 1;

        int port;
        if (args.length != 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 42824;
        }
        if (numberOfPlayer == 1) {
            server = new Server1Player(port);
            Thread serverThread = new Thread(server);
            serverThread.start();
        } else if (numberOfPlayer == 2) {
            if (setServer(port)) {
                Thread serverThread = new Thread(server);
                serverThread.start();
            }
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
