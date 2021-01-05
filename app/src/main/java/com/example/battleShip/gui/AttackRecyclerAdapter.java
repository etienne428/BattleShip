
package com.example.battleShip.gui;

import androidx.annotation.NonNull;

import com.example.battleShip.logic.BSViewAdapter;
import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.model.GameActivity;

public class AttackRecyclerAdapter extends BSViewAdapter {

    private final GameActivity context;

    /**
     * Data is passed into the constructor to be stored.
     */
    public AttackRecyclerAdapter(GameActivity context, TileStatus[] tiles) {
        super(context, tiles);
        this.context = context;
    }

    /**
     * Binds the data to the TextView in each cell.
     *
     * @param holder    the view holder
     * @param position  the index in the autoTiles[]
     */
    @Override
    public void onBindViewHolder(@NonNull AttackRecyclerAdapter.ViewHolder holder, int position) {
        holder.myTextView.setText(String.valueOf(tiles[position].getChar()));
        holder.myTextView.setBackgroundColor(tiles[position]
                .getColor(position == context.getLastPlayersTile()));
    }
}
