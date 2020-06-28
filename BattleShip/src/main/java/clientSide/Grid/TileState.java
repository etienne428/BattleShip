package clientSide.Grid;

public enum TileState {
    SEE(" "),
    BOAT("B"),
    MISSED ("O"),
    TOUCHED ("X");
    String description;

    TileState(String s) {
        this.description = s;
    }

}
