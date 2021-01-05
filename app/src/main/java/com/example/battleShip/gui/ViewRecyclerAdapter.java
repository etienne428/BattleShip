package com.example.battleShip.gui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.battleShip.logic.BSViewAdapter;
import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;

import java.util.LinkedList;

public class ViewRecyclerAdapter extends BSViewAdapter {

    private final GameActivity context;

    // data is passed into the constructor
    public ViewRecyclerAdapter(GameActivity context, TileStatus[] tiles) {
        super(context, tiles);
        this.context = context;
    }

    /**
     * Check the computer's attempt.
     *
     * @param tile      the targeted tile in myTiles[]
     * @return          the boat that has been reached
     */
    public TileStatus checkComputerAttempt(int tile) {
        Boat attempt = tiles[tile].getBoat();

        // Set default color to blue (no change)
        if (attempt != Boat.SEE) {
            // Get the corresponding lL and add the new touched tile
            LinkedList<Integer> reachedTiles = setOfBoat.remove(attempt);
            if (reachedTiles == null) {
                Log.e("ERROR", "Boat " + attempt.name()
                        + " already removed!!!!!!!!!!");
            }
            reachedTiles.addLast(tile);
            // Check if boat is drown.
            // If yes, display the letter corresponding to the boat
            if (reachedTiles.size() == attempt.getLength()) {
                setBoatDrowned(attempt, reachedTiles);
                if (setOfBoat.isEmpty()) {
                    context.announceWinner();
                }
            } else {
                // Put boat back, if boat still floating
                setOfBoat.put(attempt, reachedTiles);
            }
        }
        tiles[tile].setTargeted();
        notifyDataSetChanged();
        return tiles[tile];
    }

    /**
     * Binds the data to the TextView in each cell.
     * Update text and background color.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewRecyclerAdapter.ViewHolder holder, int position) {
        char tile = tiles[position].getChar();
        holder.myTextView.setText(String.valueOf(tile));
        holder.myTextView.setBackgroundColor(tiles[position].getColor(position == context.getLastAutoTile()));
    }
}
