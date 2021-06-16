package com.example.BattleShip.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.BattleShip.R;
import com.example.BattleShip.logic.TileStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class AttackGrid extends RecyclerView.Adapter<AttackGrid.ViewHolder> {

    protected final HashMap<Boat, LinkedList<Integer>> setOfBoat = new HashMap<>();
    protected final TileStatus[] tiles;
    protected final LayoutInflater inflater;
    private final GameActivity game;
    private final int columns;
    private final int rows;
    private boolean defender = false;

    private int lastTile = -1;

    /**
     * Constructor that stores the data needed and instantiates the tiles[].
     *
     * @param columns #columns in grid
     * @param rows    #rows in grid
     * @param game    gameActivity and Context
     */
    public AttackGrid(int columns, int rows, GameActivity game) {
        this.game = game;
        this.columns = columns;
        this.rows = rows;
        this.inflater = LayoutInflater.from(game);

        tiles = new TileStatus[columns * rows];
        Arrays.fill(tiles, new TileStatus(Boat.SEE, true));

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            setOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
    }

    public void setBoatDrowned(LinkedList<Integer> reachedTiles) {
        for (Object position : reachedTiles) {
            int pos = (int) position;
            tiles[pos].setTargeted();
        }
    }

    public void printGrid() {
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(tiles[j + (i * columns)].getChar(defender)).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_ATTACK_TILES", sb.toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // The view for one tile
        public TextView myTextView;

        ViewHolder(View view) {
            super(view);
            myTextView = itemView.findViewById(R.id.tile);
            myTextView.setTextSize(14);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemClick(view, getAdapterPosition());
//            onBindViewHolder(this, getAdapterPosition());
        }
    }

    public void onItemClick(View view, int position) {
        lastTile = position;
        // Computer makes its own attempt.
        tiles[position] = game.checkPlayersAttempt(position);
        if (tiles[position].getBoat() != Boat.SEE) {
            checkBoatFloating(tiles[position].getBoat(), position);
        }
        notifyDataSetChanged();
        game.makeAttempt();
    }

    public void checkBoatFloating(Boat boat, int position) {
        // Get the corresponding lL and add the new touched tile
        LinkedList<Integer> reachedTiles = setOfBoat.remove(boat);
        if (reachedTiles == null) {
            Log.e("ERROR", "Boat " + boat.name()
                    + " already removed!!!!!!!!!!");
        }
        reachedTiles.addLast(position);
        // Check if boat is drown.
        // If yes, display the letter corresponding to the boat
        if (reachedTiles.size() == boat.getLength()) {
            setBoatDrowned(reachedTiles);
            if (setOfBoat.isEmpty()) {
                for (TileStatus tile: tiles) {
                    defender = true;
                    notifyDataSetChanged();
                }
                game.announceWinner();
            }
        } else {
            // Put boat back, if boat still floating
            setOfBoat.put(boat, reachedTiles);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position >= 0) {
            holder.myTextView.setText(String.valueOf(tiles[position].getChar(defender)));
            holder.myTextView.setBackgroundColor(tiles[position]
                    .getColor(position == lastTile));
        }
    }

    @Override
    public int getItemCount() {
        return tiles.length;
    }
}
