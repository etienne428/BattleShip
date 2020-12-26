
package com.example.battleShip.gui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

import java.util.HashMap;
import java.util.LinkedList;


public class AttackRecyclerAdapter extends RecyclerView.Adapter<AttackRecyclerAdapter.ViewHolder> {

    // Stores each boat, with the list of already touched tiles
    private final HashMap<Boat, LinkedList<Integer>> setOfBoat = new HashMap<>();
    // The list of the computer's tile, as known by the player
    private final TileState[] autoTiles;
    private final LayoutInflater inflater;
    private final GameActivity context;

    /**
     * Data is passed into the constructor to be stored.
     */
    public AttackRecyclerAdapter(GameActivity context, TileState[] autoTiles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.autoTiles = autoTiles;

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            setOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
    }

    /**
     * Inflates the cell layout from xml when needed.
     *
     * @param parent    the grid, as view.
     * @param viewType  for the @Override
     * @return          the viewHolder
     */
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view, context);
    }

    /**
     * Binds the data to the TextView in each cell.
     *
     * @param holder    the view holder
     * @param position  the index in the autoTiles[]
     */
    @Override
    public void onBindViewHolder(@NonNull AttackRecyclerAdapter.ViewHolder holder, int position) {
        holder.myTextView.setText(String.valueOf(autoTiles[position].getCharacter()));
        try {
            Log.i("COLOR3_ATT", "color is " + autoTiles[position].getColorLast()
                    + " instead of " + autoTiles[position].getColor());
            if (position == context.getLastPlayersTile()) {
                holder.myTextView.setBackgroundColor(autoTiles[position].getColorLast());
            } else {
                holder.myTextView.setBackgroundColor(autoTiles[position].getColor());
            }
        } catch (TileException e) {
            Log.e("COLOR_ATT", "Problem by looking for color");
        }
    }

    /**
     * Total number of cells.
     *
     * @return the cell's count
     */
    @Override
    public int getItemCount() {
        return autoTiles.length;
    }


    /**
     * Stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The context, to ask for view update
        private final GameActivity context;
        // The view for the tile
        public TextView myTextView;

        /**
         * Stores and initiates the view holder.
         *
         * @param itemView  the cell's view
         * @param context   instance of gameActivity, to ask for updates
         */
        ViewHolder(View itemView, GameActivity context) {
            super(itemView);
            this.context = context;
            myTextView = itemView.findViewById(R.id.tile);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get index in tiles[]
            int pos = getAdapterPosition();
            try {
                // Check if result is a success, store the boat (or see) touched
                Log.e("TILE_ERROR_ATTACK", "To check, pos = " + pos);
                Boat attempt = context.checkPlayerAttempt(pos);
                if (attempt != Boat.SEE) {
                    // Get the corresponding lL and add the new touched tile
                    LinkedList<Integer> reachedTiles = setOfBoat.remove(attempt);
                    if (reachedTiles == null) {
                        throw new TileException(pos);
                    }
                    reachedTiles.addLast(pos);
                    // Check if boat is drown.
                    // If yes, display the letter corresponding to the boat
                    if (reachedTiles.size() == attempt.getLength()) {
                        if (setOfBoat.isEmpty()) {
                            announceWinner();
                            return;
                        }
                        setBoatDrowned(attempt, reachedTiles);
                    } else {
                        // Put boat back, if boat still floating
                        setOfBoat.put(attempt, reachedTiles);
                    }
                    Log.e("TILE_ERROR_ATTACK", "Touched, pos = " + pos);
                    autoTiles[pos] = autoTiles[pos].setTouched();
                } else {
                    Log.e("TILE_ERROR_ATTACK", "Missed, pos = " + pos);
                    autoTiles[pos] = autoTiles[pos].setMissed();
                }
            } catch (TileException e) {
                Log.e("TILE_ERROR_ATTACK", e.getMessage() + ", pos = " + pos);
            }
            onBindViewHolder(this, pos);
            notifyDataSetChanged();
        }

        private void announceWinner() {
            Toast.makeText(context, "You Won!", Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Stores the information that the boat has been touched down.
     * (is probably useless)
     *
     * @param attempt       the name of the drowned boat
     * @param reachedTiles  the tiles where it lied
     */
    private void setBoatDrowned(Boat attempt, LinkedList<Integer> reachedTiles) {
        for (Object position : reachedTiles) {
            int pos = (int) position;
            try {
                autoTiles[pos] = autoTiles[pos].setTouchedBoat(attempt);
            } catch (TileException e) {
                e.printStackTrace();
            }
        }
    }


    public void printGrid() {
        int rows = 12;
        int columns = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(autoTiles[j + (i * columns)].getCharacter()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_AUTO", sb.toString());
    }
}
