package com.example.battleShip.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.utilis.TileException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class DefendGrid extends RecyclerView.Adapter<DefendGrid.ViewHolder> {

    protected final HashMap<Boat, LinkedList<Integer>> setOfBoat = new HashMap<>();
    protected final TileStatus[] tiles;
    protected final LayoutInflater inflater;
    private final GameActivity game;
    private final int columns;
    private final int rows;

    private boolean defender = true;

    private int lastTile = -1;

    /**
     * Constructor that stores the data needed and instantiates the tiles[].
     *
     * @param columns #columns in grid
     * @param rows    #rows in grid
     * @param game    gameActivity and Context
     */
    public DefendGrid(int columns, int rows, GameActivity game) {
        this.game = game;
        this.columns = columns;
        this.rows = rows;
        this.inflater = LayoutInflater.from(game);

        tiles = new TileStatus[columns * rows];
        Arrays.fill(tiles, new TileStatus(Boat.SEE, false));

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            setOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
    }

    /**
     * Choose where the computer sets its boats.
     */
    public void setBoats(ArrayList<Integer> boatSetUp) {
        int position;
        boolean northSouth;
        for (int i = 0; i < 5; i++) {
            position = boatSetUp.get(i * 2);
            //orientation in the grid
            northSouth = boatSetUp.get(i * 2 + 1) == 1;
            // Number of tiles for the new boat
            int shipLength = Boat.values()[i].getLength();
            setBoat(position, northSouth, shipLength, i);
        }
        printGrid();
    }

    /**
     * Set a boat.
     * Extracted from setBoatOnDefault() for clarity.
     *
     * @param position   the position of the first tile.
     * @param northSouth the orientation.
     * @param shipLength the number of tiles for the ship.
     * @param shipIndex  the index of the boat in the Boat enum.
     */
    private void setBoat(int position, boolean northSouth, int shipLength,
                         int shipIndex) {
        int gap = 1;
        if (northSouth) {
            gap *= columns;
        }
        // The TileState for the new boat
        TileStatus state = null;
        state = new TileStatus(Boat.values()[shipIndex], false);
        // Fill the tiles
        for (int j = 0; j < shipLength; j++) {
            tiles[position + (j * gap)] = state;
        }
    }

    public void printGrid() {
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(tiles[j + (i * columns)].getChar(defender)).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_DEFEND_TILES", sb.toString());
    }

    public Boat checkAttempt(int position) throws TileException {
        lastTile = position;
        if (tiles[position].isTargeted()) {
            throw new TileException(position);
        }
        tiles[position].setTargeted();
        notifyDataSetChanged();
        return tiles[position].getBoat();
    }

    /**
     * Stores and recycles views as they are scrolled off screen
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        // The view for one tile
        public TextView myTextView;

        /**
         * Constructor that stores and initiate textView.
         *
         * @param itemView the view for one tile
         */
        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tile);
            myTextView.setTextSize(14);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        char tile = tiles[position].getCharacter();
//        holder.myTextView.setText(String.valueOf(tile));
//        try {
////            Log.i("COLOR2_VIEW", "From bind view : " + position + " = pos, color = " + myTiles[position].getColor());
//            if (position == context.getLastAutoTile()) {
////                Log.i("COLOR2_VIEW", "color is " + myTiles[position].getColorLast()
////                        + " instead of " + myTiles[position].getColor());
//                holder.myTextView.setBackgroundColor(tiles[position].getColorLast());
//            } else {
//                holder.myTextView.setBackgroundColor(tiles[position].getColor());
//            }
//        } catch (TileException e) {
//            Log.e("COLOR_VIEW", "Problem by looking for color : BIND " + tiles[position].name());
//        }

        if (position >= 0) {
            if (tiles[position].isOpponentDefender()) {
                Log.e("TILESTATUS", "Is opponent...");
            }
            char tile = tiles[position].getChar(defender);
            holder.myTextView.setText(String.valueOf(tile));
            holder.myTextView.setBackgroundColor(tiles[position].getColor(position == lastTile));
        }
    }

    @Override
    public int getItemCount() {
        return tiles.length;
    }
}

