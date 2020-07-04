package serverSide1Player;

import automataClient.AClient;
import serverSide.Game;
import serverSide.Server;

public class Game1P extends Game {

    private Server server;
    private AClient automata;
    private String automataBoats;
    private ClientControl1Player cc;

    /**
     * Next attempt of the automata player.
     * A is for Automata
     */
    private String nextAttemptA = null;

    /**
     * Result of human player's attempt.
     * H is for Human
     */
    private String resultOfAttemptH;


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
        server.broadcast("START " + automataBoats);
    }

    /**
     * Sends to the human player the attempt of the automata.
     * A is for Automata
     */
    public void attemptA() {
        server.broadcast("ATEMP " + nextAttemptA);
        nextAttemptA = null;
    }

    /**
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
     * Send the result of the attempt back to the automata.
     *
     * @param missOrTouch MISS or TOUCH
     */
    @Override
    public void sendResultToA(String missOrTouch) {
        automata.resultOfAttempt(missOrTouch);
    }

    /**
     * Send the result of the attempt back to the human player.
     */
    @Override
    public void sendResultToH() {
        server.broadcast(resultOfAttemptH);
    }

    @Override
    /**
     * Store the result of human's player's attempt.
     *
     * @param rOAH the Result Of Attempt from the Human player
     */
    public void setResultOfAttemptH(String rOAH) {
        resultOfAttemptH = rOAH;
    }

    /**
     * Store the boats of the automata.
     * Will be sent to the human player in method ready()
     *
     * @param boats 5 triads of letters (from 'A' to 'K'),
     *             int (from 0 to 9) and letter ('N' or 'E', north or East)
     */
    public void setAutomataBoats(String boats) {
        automataBoats = boats;
    }

    /**
     * Store the automata's next attempt.
     *
     * @param nextAttempt One letter and one int for the grid coordinate
     */
    public void setNextAttempt(String nextAttempt) {
        this.nextAttemptA = nextAttempt;
    }
}
