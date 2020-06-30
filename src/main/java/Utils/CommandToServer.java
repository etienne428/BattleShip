package Utils;

public enum CommandToServer {

    // When all boats are set
    READY(),

    // Attempt, with one letter and one number for the Tile
    // ATEMP A 1
    ATEMP(),

    // Result of an attempt : MISS or TOUCH
    // LAUNC MISS
    LAUNC(),

    // Sank boat
    // TOCO
    TOCOL(),

    // Announce the player has lost
    LOSTG(),

    // Print both boards (debugging purpose)
    PRINT()
    ;

    CommandToServer() {
    }
}
