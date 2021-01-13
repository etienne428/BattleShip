package com.example.battleShip.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.battleShip.MainActivity;
import com.example.battleShip.R;
import com.example.battleShip.gui.SetBoatsRecyclerAdapter;
import com.example.battleShip.logic.TileStatus;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.battleShip.MainActivity.EXTRA_COLUMNS;
import static com.example.battleShip.MainActivity.EXTRA_ROWS;

public class SetBoatsActivity extends AppCompatActivity
        implements SetBoatsRecyclerAdapter.ItemClickListener {

    // #columns and #rows in the grid
    private int columns;
    private int rows;

    // The list of Tiles, with their status
    private TileStatus[] myTiles;
    // The position in the myTiles array
    private int position;

    // Adapter for the view
    private SetBoatsRecyclerAdapter setBoatAdapter;

    // Bottom right view to display instructions
    private TextView textView;
    private Button confirm;


    // Next boat's index in Boat.values()
    private int indexNextBoat;

    // If the boat is to be set north-south or east-west
    private boolean northSouth = true;

    /**
     * Stores the information to be passed on to the next activity,
     * so it knows where the boats are.
     * 5 pairs of numbers where the first is the tile number and the second = 1
     * if north-south, 2 if east-west
     */
    ArrayList<Integer> boatSetUp = new ArrayList<>();

    /**
     * Entry point.
     *
     * @param savedInstanceState will be used in a later version
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_boats);

        // Store column and rows from main (not operational yet)
        Intent intent = getIntent();
        columns = intent.getIntExtra(EXTRA_COLUMNS, 10);
        rows = intent.getIntExtra(EXTRA_ROWS, 12);

        // Stores own grid
        myTiles = new TileStatus[columns * rows];
        Arrays.fill(myTiles, new TileStatus(Boat.SEE, false));

        indexNextBoat = 0;

        RecyclerView setBoatView = findViewById(R.id.set_boats_grid);
        setBoatAdapter = new SetBoatsRecyclerAdapter(this, myTiles);
        setBoatAdapter.setClickListener(this);
        setBoatView.setAdapter(setBoatAdapter);
        setBoatView.setLayoutManager(new GridLayoutManager(this, columns));

        textView = findViewById(R.id.instructions_text_set);
        updateText(false);
        confirm = findViewById(R.id.set_button);
//        Drawable background = confirm.getBackground();
//        if (background instanceof ColorDrawable) {
//            buttonColor = ((ColorDrawable) background).getColor();
//        }
        confirm.setBackgroundColor(Color.GRAY);
    }

    /**
     * Change the orientation of the boat.
     * This is initialised to north-south after each boat
     */
    public void rotate(View view) {
        northSouth = !northSouth;
        clearBoat();
        showBoat();
    }

    /**
     * Shows (updates) the boat in the view.
     */
    private void showBoat() {
        // If position is not valid, do nothing
        if (position == -1) {
            return;
        }
        // The current handled boat
        Boat boat = Boat.values()[indexNextBoat];
        // The gap in the array (as the matrix is stored as an array)
        int gap = 1;
        if (northSouth) {
            gap *= columns;
        }
        // For each tile in the current boat
        for (int i = 0; i < boat.getLength(); i++) {
            try {
                // if the Tile is already occupied, clear the board from this boat and stop
                if (myTiles[position + i * gap].getBoat() != Boat.SEE) {
                    clearBoat();
                    break;
                    // Make sure the boat doesn't span on 2 lines
                } else if (!northSouth && (position + i) % columns == 0
                        && (position) % columns != 0) {
                    throw new ArrayIndexOutOfBoundsException();
                    // Set the tile as part of the boat
                } else {
                    myTiles[position + i * gap] = new TileStatus(boat, false);
                }
                // If boat is not entirely inside of the grid, shift the boat to fix that
            } catch (ArrayIndexOutOfBoundsException e) {
                // TODO: it doesn't fully work yet
//                Log.i("BOAT", "Boat too long. Old position = " + position
//                        + " new = " + (position - (gap * (boat.getLength() - i))));
                clearBoat();
                position = position - (gap * (boat.getLength() - i));
                // recursively
                showBoat();
            }
        }
        // Asks the view to update
        setBoatAdapter.notifyDataSetChanged();
    }

    /**
     * When confirmed, stores the boat in the ArrayList to be passed to the GameActivity.
     */
    public void setBoat(View view) {
        if (!containsBoat()) {
            return;
        }
        // Add the first tile's position
        boatSetUp.add(position);
        // Add the direction
        if (northSouth) {
            boatSetUp.add(1);
        } else {
            boatSetUp.add(0);
        }
        // Sets north-south back (could be a discussing matter)
        northSouth = true;
        //Asks for next boat
        indexNextBoat++;
        if (indexNextBoat < 5) {
            updateText(indexNextBoat == 4);
            position = -1;
            confirm.setBackgroundColor(Color.GRAY);
        } else {
            closeActivity();
        }
    }

    private boolean containsBoat() {
        for (TileStatus status : myTiles) {
            if (status.getBoat() == Boat.values()[indexNextBoat]) {
                return true;
            }
        }
        return false;
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
                sb.append(myTiles[j + (i * columns)].getChar()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_MY_TILES", sb.toString());
    }

    /**
     * Close this activity and start the game.
     */
    private void closeActivity() {
        Bundle extra = new Bundle();
        extra.putSerializable(MainActivity.EXTRA_SETUP, boatSetUp);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);

        Intent intent2 = new Intent(this,
                GameActivity.class);
        intent2.putExtra(EXTRA_COLUMNS, columns);
        intent2.putExtra(EXTRA_ROWS, rows);
        intent2.putExtra(MainActivity.EXTRA_SETUP, extra);
        startActivity(intent2);
        finish();
    }

    /**
     * Handle a click on the grid.
     *
     * @param position the coordinate of the clicked tile.
     */
    @Override
    public void onItemClick(View view, int position) {
        if (position != -1) {
            clearBoat();
        }
        this.position = position;
        showBoat();
        confirm.setBackgroundColor(getResources().getColor(R.color.purple_500));
    }

    /**
     * Clear the last (and not confirmed) boat of the board.
     */
    private void clearBoat() {
        for (int i = 0; i < myTiles.length; i++) {
            if (myTiles[i].getBoat() == Boat.values()[indexNextBoat]) {
                myTiles[i] = new TileStatus(Boat.SEE, false);
            }
        }
    }

    /**
     * Display the instruction text.
     *
     * @param lastBoat the text for the last boat is slightly different
     */
    public void updateText(boolean lastBoat) {
        String sb;
        if (lastBoat) {
            sb = getString(R.string.set_boat_1) + Boat.values()[indexNextBoat] +
                    getString(R.string.set_boat_2) + Boat.values()[indexNextBoat].getLength() +
                    getString(R.string.set_boat_3);
        } else {
            sb = getString(R.string.set_boat_4) + Boat.values()[indexNextBoat] +
                    getString(R.string.set_boat_2) + Boat.values()[indexNextBoat].getLength() +
                    getString(R.string.set_boat_3);
        }
        textView.setText(sb);
    }
}