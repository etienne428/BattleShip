package com.example.battleShip.gui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.battleShip.logic.BSViewAdapter;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

import java.util.LinkedList;

public class ViewRecyclerAdapter extends BSViewAdapter {

    private final GameActivity context;

    // data is passed into the constructor
    public ViewRecyclerAdapter(GameActivity context, TileState[] tiles) {
        super(context, tiles);
        this.context = context;
    }

    /**
     * Check the computer's attempt.
     *
     * @param tile      the targeted tile in myTiles[]
     * @return          the boat that has been reached
     * @throws TileException    if the boat had already been reached
     */
    public Boat checkComputerAttempt(int tile) throws TileException {
        Boat attempt = tiles[tile].getBoat();

        // Set default color to blue (no change)
        try {
            if (attempt != Boat.SEE) {
                // Get the corresponding lL and add the new touched tile
                LinkedList<Integer> reachedTiles = setOfBoat.remove(attempt);
                if (reachedTiles == null) {
                    throw new TileException(tile);
                }
                reachedTiles.addLast(tile);
                // Check if boat is drown.
                // If yes, display the letter corresponding to the boat
                if (reachedTiles.size() == attempt.getLength()) {
                    if (setOfBoat.isEmpty()) {
                        context.announceWinner();
                    }
                    setBoatDrowned(attempt, reachedTiles);
                } else {
                    // Put boat back, if boat still floating
                    setOfBoat.put(attempt, reachedTiles);
                }
                tiles[tile] = tiles[tile].setTouched();
            } else {
                tiles[tile] = tiles[tile].setMissed();
            }
        } catch (TileException e) {
            // Shouldn't occur
            Log.e("TILE_ERROR_VIEW", e.getMessage() + ", pos = " + tile);
        }
        notifyDataSetChanged();
        return attempt;
    }

    /**
     * Binds the data to the TextView in each cell.
     * Update text and background color.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewRecyclerAdapter.ViewHolder holder, int position) {
        char tile = tiles[position].getCharacter();
        holder.myTextView.setText(String.valueOf(tile));
        try {
//            Log.i("COLOR2_VIEW", "From bind view : " + position + " = pos, color = " + myTiles[position].getColor());
            if (position == context.getLastAutoTile()) {
//                Log.i("COLOR2_VIEW", "color is " + myTiles[position].getColorLast()
//                        + " instead of " + myTiles[position].getColor());
                holder.myTextView.setBackgroundColor(tiles[position].getColorLast());
            } else {
                holder.myTextView.setBackgroundColor(tiles[position].getColor());
            }
        } catch (TileException e) {
            Log.e("COLOR_VIEW", "Problem by looking for color : BIND " + tiles[position].name());
        }
    }
}
