package automataClient;

import Boat.SetOfBoats;
import Utils.CommandException;
import Utils.CommandToClient;
import Utils.Parser;
import serverSide.Game;

public class AClient {

    private final AGameFrame gameFrame;
    private final Game game;

    public AClient(Game game) {
        gameFrame = new AGameFrame(this, game);
        this.game = game;
        Thread GRAPHIC = new Thread(gameFrame);
        GRAPHIC.start();

        //serverListener.start();
        System.out.println("AClient running");
        gameFrame.launch();
    }

    public void performAction(String s) {
        System.out.println("AClient Received " + s);
        try {
            CommandToClient command = CommandToClient.valueOf(Parser.getCommand(s));
            s = s.substring(5);
            switch (command) {
                case START:
                    gameFrame.startGame(s);
                    break;

                    // Check attempt from other player
                case ATEMP:
                    gameFrame.attemptCheck(s);
                    break;
                case LAUNC:
                    gameFrame.resultOfAttempt(s);
                case TOCOL:
                    // will announce that boat has been sank
                    System.out.println("A: You sank the " + SetOfBoats.getNames()[Integer.parseInt(s)]);
                    break;
                case WINNE:
                    // will announce that game is won
                    System.out.println("A: You have won!");
                    break;
                default:
                    System.out.println("A: Incorrect command : " + s);
            }

        } catch (CommandException e) {
            System.out.println("A: Command " + s + " not valid");
            e.printStackTrace();
        }
    }

    public void sendMessage(String s) {
        ;
    }

    /**
     * Sends to the automata player the attempt of the human player to be checked.
     * H is for Human
     *
     * @param s One letter and one int for the grid coordinate
     */
    public void attemptCheck(String s) {
        gameFrame.attemptCheck(s);
    }

    /**
     * Send the result of the attempt back to the automata.
     *
     * @param missOrTouch MISS or TOUCH
     */
    public void resultOfAttempt(String missOrTouch) {
        gameFrame.resultOfAttempt(missOrTouch);
    }
}
