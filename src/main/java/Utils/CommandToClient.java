package Utils;


public enum CommandToClient {

    // Attempt, with one letter and one number for the Tile
    // ATEMP A 1
    ATEMP("Attempt"),

    // Announce the players the game can start, with the position of each
    // boat of the opponent, S E as direction
    // START A5S B1E J4S G6E
    START("Start"),

    // Result of an attempt : MISS or TOUCH
    // LAUNC MISS
    LAUNC("Launch"),

    // Sank boat
    // TOCO
    TOCOL("TouchéCoulé"),

    // Announce the player has won
    WINNE("Winner"),

    // Print both boards (debugging purpose)
    PRINT("Print")
    ;

    CommandToClient(String NAME) {
    }
}
