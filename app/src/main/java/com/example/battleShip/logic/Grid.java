package com.example.battleShip.logic;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.gui.AttackRecyclerAdapter;
import com.example.battleShip.gui.ViewRecyclerAdapter;
import com.example.battleShip.model.AutoPlayer;
import com.example.battleShip.model.Boat;
import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.TileState;
import com.example.battleShip.utilis.TileException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public abstract class Grid implements BSViewAdapter.ItemClickListener {

    private final GameActivity game;
    private final int columns;
    private final int rows;
    private final TileStatus[] tiles;
    protected final HashMap<Boat, LinkedList<Integer>> setOfBoat = new HashMap<>();

    private BSViewAdapter adapter;

    private int lastTile;

    /**
     * Need to fill tiles array.
     *
     * @param columns
     * @param rows
     * @param game
     */
    public Grid(int columns, int rows, GameActivity game, boolean opponent) {
        this.columns = columns;
        this.rows = rows;
        this.game = game;
        tiles = new TileStatus[columns * rows];
        Arrays.fill(tiles, new TileStatus(Boat.SEE, opponent));

        // instantiate a ll for each boat
        for (int i = 0; i < 5; i++) {
            setOfBoat.put(Boat.values()[i], new LinkedList<>());
        }
    }

    protected void setGrid(RecyclerView view, boolean attack) {
        adapter = new ViewRecyclerAdapter(game, tiles);
        view.setAdapter(adapter);
        view.setLayoutManager(new GridLayoutManager(game, columns));
        if (attack) {
            adapter.setClickListener(this);
        }
    }

    public Boat checkAttempt(int tile) {
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
//                setBoatDrowned(attempt, reachedTiles);
                if (setOfBoat.isEmpty()) {
//                    context.announceWinner();
                }
            } else {
                // Put boat back, if boat still floating
                setOfBoat.put(attempt, reachedTiles);
            }
        }
        tiles[tile].setTargeted();
//        notifyDataSetChanged();
        return attempt;
    }

    void printGrid() {
        int rows = 12;
        int columns = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(tiles[j + (i * columns)].getChar()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_MY_TILES", sb.toString());
    }

    public int getLastTile() {
        return lastTile;
    }

    public void notifyChange() {
        adapter.notifyDataSetChanged();
    }

    public void setTileState(int position, TileStatus state) {
        tiles[position] = state;
    }

    public int getColumns() {
        return columns;
    }

    @Override
    public void onItemClick(View view, int tile) {
        lastTile = tile;
        // Computer makes its own attempt.
        tiles[tile] = game.checkPlayersAttempt(tile).setOpponent();
        adapter.notifyDataSetChanged();
        game.makeAttempt();
    }
}
