package automataClient;

import Boat.SetOfBoats;
import Utils.CommandException;
import Utils.CommandToClient;
import Utils.Parser;
import serverSide.ClientControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class AClient {

    private final AGameFrame gameFrame = new AGameFrame(this);
    private final Thread GRAPHIC = new Thread(gameFrame);
    private final ClientControl clientControl;

    public AClient(ClientControl cc) {
        clientControl = cc;
        GRAPHIC.start();

        //serverListener.start();
        System.out.println("Client running");
        gameFrame.launch();
    }

    public static void quit() {
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
                case ATEMP:
                    gameFrame.attempt(s);
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
            System.out.println("Command " + s + " not valid");
            e.printStackTrace();
        }
    }

    public void sendMessage(String s) {
        clientControl.executeCommand(s);
    }
}
