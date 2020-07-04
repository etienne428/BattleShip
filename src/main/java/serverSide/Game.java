package serverSide;

import serverSide1Player.ClientControl1Player;

public abstract class Game {
    public abstract void setAutomataBoats(String boats);

    public abstract void addPlayer(ClientControl1Player clientControl1Player);

    public abstract void setNextAttempt(String boatsPlacement);

    public abstract void ready();

    public abstract void attemptA();

    public abstract void attemptH(String s);

    public abstract void sendResultToA(String s);

    public abstract void sendResultToH();

    public abstract void setResultOfAttemptH(String missOrTouch);
}
