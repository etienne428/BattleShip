package com.example.battleShip.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.battleShip.MainActivity;
import com.example.battleShip.R;
import com.example.battleShip.gui.SetBoatsRecyclerAdapter;
import com.example.battleShip.gui.ViewRecyclerAdapter;
import com.example.battleShip.utilis.TileException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class SetBoatsActivity extends AppCompatActivity
        implements SetBoatsRecyclerAdapter.ItemClickListener {

    private int columns;
    private int rows;
    private TileState[] myTiles;
    private RecyclerView setBoatView;
    private SetBoatsRecyclerAdapter setBoatAdapter;
    private TextView textView;
    private int indexNextBoat;
    private boolean northSouth = true;
    private int position;

    private int[] boatSetUp = new int[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_boats);

        // Store column and rows from main (not operational yet)
        Intent intent = getIntent();
        columns = intent.getIntExtra(MainActivity.EXTRA_COLUMNS, 10);
        rows = intent.getIntExtra(MainActivity.EXTRA_ROWS, 12);

        // Stores own grid
        myTiles = new TileState[columns * rows];
        Arrays.fill(myTiles, TileState.SEE_CLEAR);

        indexNextBoat = 0;
        //
        setBoatView = findViewById(R.id.set_boats_grid);
        setBoatAdapter = new SetBoatsRecyclerAdapter(this, myTiles);
        setBoatAdapter.setClickListener(this);
        setBoatView.setAdapter(setBoatAdapter);
        setBoatView.setLayoutManager(new GridLayoutManager(this, columns));

        textView = findViewById(R.id.instructions_text);
        updateText(0);
    }

    public void rotate(View view) {
        Log.i("TO_DO", "Rotate");
        northSouth = !northSouth;
        clearBoat();
        showBoat();
    }

    private void showBoat() {
        Log.i("BOAT", "position = " + position);
        printGrid();
        if (position == -1) {
            return;
        }
        int gap = 1;
        Boat boat = Boat.values()[indexNextBoat];
        if (northSouth) {
            gap *= columns;
        }
        for (int i = 0; i < boat.getLength(); i++) {
            try {
                if (myTiles[position + i*gap] != TileState.SEE_CLEAR) {
                    clearBoat();
                } else if (!northSouth && (position + i) % columns == 0
                        && (position) % columns != 0) {
                    throw new ArrayIndexOutOfBoundsException();
                } else {
                    myTiles[position + i * gap] = TileState.setClear(boat);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                // TODO: it doesn't fully work yet
                Log.i("BOAT", "Boat too long. Old position = " + position
                        + " new = " + (position - (gap * (boat.getLength() - i))));
                clearBoat();
                 position = position - (gap * (boat.getLength() - i));
                 showBoat();
            } catch (TileException e) {
                Log.e("BOAT", "Too many boats set : " + position);
            }
        }
        setBoatAdapter.notifyDataSetChanged();
    }


    public void printGrid() {
        int rows = 12;
        int columns = 10;
        StringBuilder sb = new StringBuilder();
        sb.append(".\n|");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                sb.append(myTiles[j + (i * columns)].getCharacter()).append(" ");
            }
            sb.append("|\n|");
        }
        Log.i("PRINT_MY_TILES", sb.toString());
    }

    public void setBoat(View view) {
        boatSetUp[indexNextBoat * 2] = position;
        if (northSouth) {
            boatSetUp[indexNextBoat * 2 + 1] = 1;
        } else {
            boatSetUp[indexNextBoat * 2 + 1] = 0;
        }
        northSouth = true;
        indexNextBoat++;
        if (indexNextBoat >= 5) {
            closeActivity();
        }
        updateText(indexNextBoat);
        position = -1;
        Log.i("TO_DO", "Set Boat");
    }

    private void closeActivity() {
        Intent intent = new Intent();
//        String boatSetUpString = Arrays.toString(boatSetUp);
        intent.putExtra(MainActivity.BOAT_SETUP, boatSetUp);
        // TODO: store and transmit result, check it's good
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (position != -1) {
            clearBoat();
        }
        this.position = position;
        showBoat();
    }

    private void clearBoat() {
        for (int i = 0; i < myTiles.length; i++) {
            try {
                if (myTiles[i].getBoat() == Boat.values()[indexNextBoat]) {
                    myTiles[i] = TileState.SEE_CLEAR;
                }
            } catch (TileException e) {
                Log.e("BOAT", "clearBoat " + e.getMessage());
            }
        }
    }

    public void updateText(String text) {
        textView.setText(text);
    }

    public void updateText(int position) {
        String sb = "Next boat is " + Boat.values()[position] +
                " and has " + Boat.values()[position].getLength() +
                " tiles. Choose where to put it.";
        textView.setText(sb);
    }
}