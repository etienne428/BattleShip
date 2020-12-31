
package com.example.battleShip.gui;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.battleShip.logic.BSViewAdapter;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

public class AttackRecyclerAdapter extends BSViewAdapter {

    private final GameActivity context;

    /**
     * Data is passed into the constructor to be stored.
     */
    public AttackRecyclerAdapter(GameActivity context, TileState[] tiles) {
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
        holder.myTextView.setText(String.valueOf(tiles[position].getCharacter()));
        try {
            if (position == context.getLastPlayersTile()) {
                holder.myTextView.setBackgroundColor(tiles[position].getColorLast());
            } else {
                holder.myTextView.setBackgroundColor(tiles[position].getColor());
            }
        } catch (TileException e) {
            Log.e("COLOR_ATT", "Problem by looking for color");
        }
    }
}
