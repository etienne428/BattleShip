package com.example.battleShip.gui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.SetBoatsActivity;

import java.util.HashMap;
import java.util.LinkedList;


public class SetBoatsRecyclerAdapter extends RecyclerView.Adapter<SetBoatsRecyclerAdapter.ViewHolder> {
    private final HashMap<Boat, LinkedList<Integer>> mySetOfBoat = new HashMap<>();

    private final TileStatus[] myTiles;
    private final LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor

    public SetBoatsRecyclerAdapter(SetBoatsActivity context, TileStatus[] myTiles) {
        this.mInflater = LayoutInflater.from(context);
        this.myTiles = myTiles;

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            mySetOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
    }
    // inflates the cell layout from xml when needed

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        char tile = myTiles[position].getChar(true);
        holder.myTextView.setText(String.valueOf(tile));
        if (tile == ' ') {
            holder.myTextView.setBackgroundColor(Color.BLUE);
        } else {
            holder.myTextView.setBackgroundColor(Color.GREEN);
        }
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return myTiles.length;
    }


    /**
     * Stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The view for one tile
        TextView myTextView;

        /**
         * Constructor that stores and initiate textView.
         *
         * @param itemView the view for one tile
         */
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tile);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

    }


    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}