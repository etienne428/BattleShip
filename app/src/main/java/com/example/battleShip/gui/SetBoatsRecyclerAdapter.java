package com.example.battleShip.gui;

import android.graphics.Color;

import androidx.annotation.NonNull;

import com.example.battleShip.logic.BSViewAdapter;
import com.example.battleShip.model.SetBoatsActivity;
import com.example.battleShip.model.TileState;

public class SetBoatsRecyclerAdapter extends BSViewAdapter {


    // data is passed into the constructor
    public SetBoatsRecyclerAdapter(SetBoatsActivity context, TileState[] myTiles) {
        super(context, myTiles);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        char tile = tiles[position].getCharacter();
        holder.myTextView.setText(String.valueOf(tile));
        if (tile == ' ') {
            holder.myTextView.setBackgroundColor(Color.BLUE);
        } else {
            holder.myTextView.setBackgroundColor(Color.GREEN);
        }
    }
}