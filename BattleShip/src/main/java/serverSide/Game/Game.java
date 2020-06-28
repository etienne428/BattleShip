package serverSide.Game;

import serverSide.Server;

import java.util.Random;

public class Game {

    private int playerReady = 0;
    private Server server;

    public Game(Server server) {
        this.server = server;
    }

    public void ready() {
        playerReady++;
        if (playerReady == server.getNOC()) {
            launchGame();
        }
    }

    private void launchGame() {
        server.broadcast("START ");
    }
}
