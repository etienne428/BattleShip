package Utils;


public enum CommandToClient {

    // Attempt, with one letter and one number for the Tile
    // ATEMP A 1
    ATEMP(),

    // Announce the players the game can start, with the position of each
    // boat of the opponent, S E as direction
    // START A5S B1E J4S G6E
    START(),

    // Result of an attempt : MISS or TOUCH
    // LAUNC MISS
    LAUNC(),

    // Sank boat
    // TOCO
    TOCOL(),

    // Announce the player has won
    WINNE(),

    // Print both boards (debugging purpose)
    PRINT()
    ;

    CommandToClient() {
    }
}
