package com.example.battleShip.gui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

import java.util.HashMap;
import java.util.LinkedList;


public class ViewRecyclerAdapter extends RecyclerView.Adapter<ViewRecyclerAdapter.ViewHolder> {

    // Stores each boat, with the list of already touched tiles
    private final HashMap<Boat, LinkedList<Integer>> mySetOfBoat = new HashMap<>();
    private final TileState[] myTiles;
    private final LayoutInflater inflater;
    private final GameActivity context;
    private ViewHolder viewHolder;

    // data is passed into the constructor
    public ViewRecyclerAdapter(GameActivity context, TileState[] tiles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.myTiles = tiles;

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            mySetOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
        printGrid();
    }

    /**
     * Check the computer's attempt.
     *
     * @param tile      the targeted tile in myTiles[]
     * @return          the boat that has been reached
     * @throws TileException    if the boat had already been reached
     */
    public Boat checkComputerAttempt(int tile) throws TileException {
        Boat attempt = myTiles[tile].getBoat();

        // Set default color to blue (no change)
        try {
            if (attempt != Boat.SEE) {
                // Get the corresponding lL and add the new touched tile
                LinkedList<Integer> reachedTiles = mySetOfBoat.remove(attempt);
                if (reachedTiles == null) {
                    throw new TileException(tile);
                }
                reachedTiles.addLast(tile);
                // Check if boat is drown.
                // If yes, display the letter corresponding to the boat
                if (reachedTiles.size() == attempt.getLength()) {
                    if (mySetOfBoat.isEmpty()) {
                        context.announceWinner();
                    }
                    setBoatDrowned(attempt, reachedTiles);
                } else {
                    // Put boat back, if boat still floating
                    mySetOfBoat.put(attempt, reachedTiles);
                }
                myTiles[tile] = myTiles[tile].setTouched();
            } else {
                myTiles[tile] = myTiles[tile].setMissed();
            }
        } catch (TileException e) {
            // Shouldn't occur
            Log.e("TILE_ERROR_VIEW", e.getMessage() + ", pos = " + tile);
        }
        notifyDataSetChanged();
        return attempt;
    }

    /**
     * Inflates the cell layout from xml when needed
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Binds the data to the TextView in each cell.
     * Update text and background color.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewRecyclerAdapter.ViewHolder holder, int position) {
        holder.myTextView.setText(String.valueOf(myTiles[position].getCharacter()));
        try {
            Log.i("COLOR2_VIEW", "From bind view : " + position + " = pos, color = " + myTiles[position].getColor());
            if (position == context.getLastAutoTile()) {
                Log.i("COLOR2_VIEW", "color is " + myTiles[position].getColorLast()
                        + " instead of " + myTiles[position].getColor());
                holder.myTextView.setBackgroundColor(myTiles[position].getColorLast());
            } else {
                holder.myTextView.setBackgroundColor(myTiles[position].getColor());
            }
        } catch (TileException e) {
            Log.e("COLOR_VIEW", "Problem by looking for color : BIND " + myTiles[position].name());
        }
    }

    /**
     * Total number of cells
     */
    @Override
    public int getItemCount() {
        return myTiles.length;
    }


    /**
     * Stores and recycles views as they are scrolled off screen
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // The view for one tile
        public TextView myTextView;

        /**
         * Constructor that stores and initiate textView.
         *
         * @param itemView the view for one tile
         */
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tile);
            myTextView.setTextSize(14);
        }
    }

    /**
     * Set a ship as drowned by showing its char (instead of X).
     *
     * @param attempt      the drowned boat
     * @param reachedTiles the list of tiles where the boat was
     */
    private void setBoatDrowned(Boat attempt, LinkedList<Integer> reachedTiles) {
        for (Object position : reachedTiles) {
            int pos = (int) position;
            try {
                myTiles[pos] = myTiles[pos].setTouchedBoat(attempt);
            } catch (TileException e) {
                e.printStackTrace();
            }
        }
        Log.i("PRINT_VIEW_RECYCLER", attempt.toString());
    }

    /**
     * For debugging purposes, print the grid.
     */
    public void printGrid() {
        int rows = 12;
        int columns = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(myTiles[j + (i * columns)].getCharacter()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_VIEW_RECYCLER", sb.toString());
    }
}
