package com.example.battleShip.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleShip.MainActivity;
import com.example.battleShip.R;
import com.example.battleShip.logic.TileStatus;
import com.example.battleShip.utilis.TileException;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private int columns;
    private int rows;

    private AttackGrid attackGrid;
    private DefendGrid defendGrid;

    private int lastPlayersTile;
    private int lastAutoTile;

    private TextView info;
    private AutoPlayer auto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Store column and rows from main (not operational yet)
        Intent intent = getIntent();
        columns = intent.getIntExtra(MainActivity.EXTRA_COLUMNS, 10);
        rows = intent.getIntExtra(MainActivity.EXTRA_ROWS, 12);
        Bundle extra = intent.getBundleExtra(MainActivity.EXTRA_SETUP);

        setEnemyGrid();
        setMyGrid(extra);
        assert extra != null;

        // Find the view that gives info to the player
        info = findViewById(R.id.instructions_text_game);
        writeInstruction(getString(R.string.welcome_text));

        // Instantiate the computer player and set its boats
        auto = new AutoPlayer(this, columns, rows);
        auto.run();
    }

    private void setMyGrid(Bundle extra) {
        RecyclerView defendView = findViewById(R.id.bottom_grid);
        defendGrid = new DefendGrid(columns, rows, this);
        defendView.setAdapter(defendGrid);
        defendView.setLayoutManager(new GridLayoutManager(this, columns));

        ArrayList<Integer> boatSetUp = (ArrayList<Integer>) extra.getSerializable(MainActivity.EXTRA_SETUP);
        defendGrid.setBoats(boatSetUp);
        defendGrid.notifyDataSetChanged();
    }

    private void setEnemyGrid() {
        RecyclerView attackView = findViewById(R.id.top_grid);
        attackGrid = new AttackGrid(columns, rows, this);
        attackView.setAdapter(attackGrid);
        attackView.setLayoutManager(new GridLayoutManager(this, columns));
    }


    private void writeInstruction(String instruction) {
        info.setText(instruction);
    }


    public Boat checkComputerAttempt(int tile) throws TileException {
        return defendGrid.checkAttempt(tile);
    }

    /**
     * Check if a player's attempt is successful.
     * Called by AttackRecyclerAdapter.ViewHolder.onClick(),
     * which gets the boat back and sets the color.
     *
     * @param tile the targeted tile
     * @return the result (SEE, or the name of the boat)
     */
    public TileStatus checkPlayersAttempt(int tile) {
        return auto.checkPlayersAttempt(tile);
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

    public void makeAttempt() {
        auto.makeAttempt();
    }
}