package com.example.battleShip.gui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.model.SetBoatsActivity;
import com.example.battleShip.model.TileState;

public class SetBoatsRecyclerAdapter extends RecyclerView.Adapter<SetBoatsRecyclerAdapter.ViewHolder> {

    protected final TileState[] tiles;
    protected final LayoutInflater inflater;
    protected final Context context;
    protected ItemClickListener clickListener;

    // data is passed into the constructor
    public SetBoatsRecyclerAdapter(SetBoatsActivity context, TileState[] myTiles) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.tiles = myTiles;
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
            itemView.setOnClickListener(this);
            myTextView.setTextSize(14);
        }

        @Override
        public void onClick(View view) {
            Log.e("CLICK", "Click in onClick");
            if (clickListener != null) {
                clickListener.onItemClick(getAdapterPosition());
            }
        }
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