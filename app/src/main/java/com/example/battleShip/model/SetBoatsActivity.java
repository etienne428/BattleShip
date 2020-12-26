package com.example.battleShip.model;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.battleShip.MainActivity;
import com.example.battleShip.R;
import com.example.battleShip.gui.SetBoatsRecyclerAdapter;
import com.example.battleShip.gui.ViewRecyclerAdapter;

import java.util.Arrays;

public class SetBoatsActivity extends AppCompatActivity
        implements SetBoatsRecyclerAdapter.ItemClickListener  {

    private int columns;
    private int rows;
    private RecyclerView setBoatView;
    private SetBoatsRecyclerAdapter setBoatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_boats);

        // Store column and rows from main (not operational yet)
        Intent intent = getIntent();
        columns = Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_COLUMNS));
        rows = Integer.parseInt(intent.getStringExtra(MainActivity.EXTRA_ROWS));

        // Stores own grid
        TileState[] myTiles = new TileState[columns * rows];
        Arrays.fill(myTiles, TileState.SEE_CLEAR);

        //
        setBoatView = findViewById(R.id.bottom_grid);
        setBoatAdapter = new SetBoatsRecyclerAdapter(this, myTiles);
        setBoatView.setAdapter(setBoatAdapter);
        setBoatView.setLayoutManager(new GridLayoutManager(this, columns));

    }

    public void rotate(View view) {
    }

    public void setBoat(View view) {
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.i("TAG", "You clicked number " + setBoatAdapter.getItem(position) + ", which is at cell position " + position);
    }
}