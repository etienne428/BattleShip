package com.example.battleShip.logic;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.gui.AttackRecyclerAdapter;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class BSViewAdapter extends RecyclerView.Adapter<BSViewAdapter.ViewHolder> {

    // Stores each boat, with the list of already touched tiles
    protected final HashMap<Boat, LinkedList<Integer>> setOfBoat = new HashMap<>();
    protected final TileState[] tiles;
    protected final LayoutInflater inflater;
    protected final Context context;
    protected ItemClickListener clickListener;

    // data is passed into the constructor
    public BSViewAdapter(Context context, TileState[] tiles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.tiles = tiles;

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            setOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
        printGrid();
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
        return new ViewHolder(view);
    }

    /**
     * Total number of cells
     *
     * @return the cell's count
     */
    @Override
    public int getItemCount() {
        return tiles.length;
    }

    /**
     * Stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(getAdapterPosition());
        }
    }

    /**
     * Set a ship as drowned by showing its char (instead of X).
     *
     * @param attempt      the drowned boat
     * @param reachedTiles the list of tiles where the boat was
     */
    protected void setBoatDrowned(Boat attempt, LinkedList<Integer> reachedTiles) {
        for (Object position : reachedTiles) {
            int pos = (int) position;
            try {
                tiles[pos] = tiles[pos].setTouchedBoat(attempt);
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
                sb.append(tiles[j + (i * columns)].getCharacter()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_VIEW_RECYCLER", sb.toString());
    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
