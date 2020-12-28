package com.example.battleShip.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleShip.MainActivity;
import com.example.battleShip.R;
import com.example.battleShip.gui.AttackRecyclerAdapter;
import com.example.battleShip.gui.ViewRecyclerAdapter;
import com.example.battleShip.utilis.BoatNotSetException;
import com.example.battleShip.utilis.TileException;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private AttackRecyclerAdapter attackAdapter;
    private RecyclerView attackView;

    private TextView info;
    private int columns;
    private int rows;
    private AutoPlayer auto;

    private ViewRecyclerAdapter defendAdapter;
    private RecyclerView defendView;
    private int lastPlayersTile;
    private int lastAutoTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Store column and rows from main (not operational yet)
        Intent intent = getIntent();
        columns = intent.getIntExtra(MainActivity.EXTRA_COLUMNS, 10);
        rows = intent.getIntExtra(MainActivity.EXTRA_ROWS, 12);

        setEnemyGrid();
        setMyGrid();

        // Find the view that gives info to the player
        info = findViewById(R.id.instructions_text);
        writeInstruction(getString(R.string.welcome_text));

        // Instantiate the computer player and set its boats
        auto = new AutoPlayer(this, columns, rows);
        auto.run();
    }

    /**
     * Instantiate player's own part
     */
    private void setMyGrid() {
        // Stores own grid
        TileState[] myTiles = new TileState[columns * rows];
        Arrays.fill(myTiles, TileState.SEE_CLEAR);
        setBoatOnDefault(myTiles);

        //
        defendView = findViewById(R.id.bottom_grid);
        defendAdapter = new ViewRecyclerAdapter(this, myTiles);
        // deleted : ", defendView.getHeight() / rows"
        defendView.setAdapter(defendAdapter);
        defendView.setLayoutManager(new GridLayoutManager(this, columns));

        printGrid(myTiles);
    }

    /**
     * Instantiate the opponent's part
     */
    private void setEnemyGrid() {
        // Stores the opponent's grid
        TileState[] autoTiles = new TileState[columns * rows];
        Arrays.fill(autoTiles, TileState.OPP_CLEAR);

        // Link it to the recycler view
        attackView = findViewById(R.id.top_grid);
        attackAdapter = new AttackRecyclerAdapter(this, autoTiles);
        // deleted : ", attackView.getHeight() / rows"
        attackView.setAdapter(attackAdapter);
        attackView.setLayoutManager(new GridLayoutManager(this, columns));
    }

    private void writeInstruction(String instruction) {
        info.setText(instruction);
    }

    public Boat checkComputerAttempt(int tile) throws TileException {
        lastAutoTile = tile;
        Boat boat = defendAdapter.checkComputerAttempt(tile);
        int color;
        if (boat.compareTo(Boat.SEE) == 0) {
            Log.i("ATTEMPT2", "Boat == " + boat.name());
            color = Color.rgb(51, 161, 251);
        } else {
            Log.i("ATTEMPT2", "Boat != " + boat.name());
            color = Color.RED;
        }
        defendAdapter.notifyDataSetChanged();
        return boat;
    }

    /**
     * Check if a player's attempt is successful.
     * Called by AttackRecyclerAdapter.ViewHolder.onClick(),
     * which gets the boat back and sets the color.
     *
     * @param tile the targeted tile
     * @return the result (SEE, or the name of the boat)
     * @throws TileException if the tile was already targeted
     */
    public Boat checkPlayerAttempt(int tile) throws TileException {
        lastPlayersTile = tile;
        // Computer makes its own attempt.
        auto.makeAttempt();
        attackAdapter.notifyDataSetChanged();
        return auto.checkPlayersAttempt(tile);
    }

    public void printGrid(TileState[] tiles) {
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
        Log.i("PRINT_MY_TILES", sb.toString());
    }


    /**
     * Choose where the computer sets its boats.
     * For debugging purposes only, it is automatically set
     */
    void setBoatOnDefault(TileState[] myTiles) {
        Random random = new Random();
        TileState state;
        for (int i = 0; i < 5; i++) {
            boolean northSouth = random.nextBoolean();
            int shipLength = Boat.values()[i].getLength();
            int line;
            int column;
            try {
                if (northSouth) {
                    column = random.nextInt(columns);
                    line = random.nextInt(rows - shipLength);
                    int tile = line * columns + column;
                    boatCanBeSet(tile, shipLength, true, myTiles);
                    try {
                        state =  TileState.getTile(10 * (i + 1));
                        for (int j = 0; j < shipLength; j++) {
                            myTiles[tile + (j * columns)] = state;
                        }
                    } catch (TileException e) {
                        Log.e("ERROR_BOAT", Objects.requireNonNull(e.getMessage()));
                    }
                } else {
                    column = random.nextInt(columns - shipLength);
                    line = random.nextInt(rows);
                    int tile = line * columns + column;
                    boatCanBeSet(tile, shipLength, false, myTiles);
                    try {
                        state = TileState.getTile(10 * (i + 1));
                        for (int j = 0; j < shipLength; j++) {
                            myTiles[tile + j] = state;
                        }
                    } catch (TileException e) {
                        Log.e("ERROR_BOAT", Objects.requireNonNull(e.getMessage()));
                    }
                }
            } catch (BoatNotSetException e) {
                i--;
            }
        }
        printGrid(myTiles);
    }

    /**
     * Check that place is free for new boat.
     * Acts like returning a boolean, but throws an exception if false instead.
     *
     * @param tile       the first tile
     * @param shipLength the length of the ship
     * @param northSouth true if boat spans across several rows but one column
     * @throws BoatNotSetException if one tile is already taken
     */
    private void boatCanBeSet(int tile, int shipLength, boolean northSouth, TileState[] myTiles) throws BoatNotSetException {
        for (int i = 0; i < shipLength; i++) {
            if (northSouth) {
                if (myTiles[tile + (i * columns)].isOccupied()) {
                    throw new BoatNotSetException();
                }
            } else {
                if (myTiles[tile + i].isOccupied()) {
                    throw new BoatNotSetException();
                }
            }
        }
    }

    public void announceWinner() {
        Toast.makeText(this, "Congratulations, you Won!", Toast.LENGTH_LONG).show();
    }

    public int getLastPlayersTile() {
        return lastPlayersTile;
    }

    public int getLastAutoTile() {
        return lastAutoTile;
    }
}