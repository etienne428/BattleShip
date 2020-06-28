package Utils;

public enum CommandToServer {

    // When all boats are set
    READY("Ready"),

    // Attempt, with one letter and one number for the Tile
    // ATEMP A 1
    ATEMP("Attempt"),

    // Result of an attempt : MISS or TOUCH
    // LAUNC MISS
    LAUNC("Launch"),

    // Sank boat
    // TOCO
    TOCOL("TouchéCoulé"),

    // Announce the player has lost
    LOSTG("Lost"),

    // Print both boards (debugging purpose)
    PRINT("Print")
    ;

    CommandToServer(String NAME) {
    }
}
