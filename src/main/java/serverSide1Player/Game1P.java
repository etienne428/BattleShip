package serverSide1Player;

import automataClient.AClient;
import serverSide.Game;
import serverSide.Server;

public class Game1P extends Game {

    private Server server;
    private AClient automata;
    private ClientControl1Player cc;

    /**
     * Next attempt of the automata player.
     * A is for Automata
     */
    private String nextAttemptA = null;


    public Game1P(Server server) {
        this.server = server;
        automata = new AClient(this);
    }

    @Override
    public void addPlayer(ClientControl1Player clientControl) {
        cc = clientControl;
    }

    @Override
    public void ready() {

        automata.startGame();
        server.broadcast("START ");
    }

    /**
     *
     *
     * Sends to the human player the attempt of the automata.
     * A is for Automata
     */
    public void attemptA() {
        server.broadcast("ATEMP " + nextAttemptA);
        nextAttemptA = null;
    }

    /**
     *
     *
     * Sends to the automata player the attempt of the human player to be checked.
     * H is for Human
     *
     * @param s One letter and one int for the grid coordinate
     */
    @Override
    public void attemptH(String s) {
        automata.attemptCheck(s);

    }

    /**
     *
     *
     * Send the result of the attempt back to the automata.
     *
     * @param missOrTouch MISS or TOUCH
     */
    @Override
    public void sendResultToA(String missOrTouch) {
        //System.out.println("sendResultToA = " + missOrTouch);
        automata.resultOfAttempt(missOrTouch);
    }

    /**
     *
     *
     * Send the result of the attempt back to the human player.
     */
    @Override
    public void sendResultToH(String resultOfAttemptH) {
        //System.out.println("sendResultToH = " + resultOfAttemptH);
        server.broadcast(resultOfAttemptH);
    }

    @Override
    public void printAGrid() {
        automata.printGrid();
    }

    /**
     *
     *
     * Store the automata's next attempt.
     *
     * @param nextAttempt One letter and one int for the grid coordinate
     */
    public void setNextAttempt(String nextAttempt) {
        this.nextAttemptA = nextAttempt;
    }
}
