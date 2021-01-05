package com.example.battleShip.logic;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.battleShip.R;
import com.example.battleShip.model.Boat;

import java.util.LinkedList;

public interface GridInterface {

    void setBoatDrowned(Boat attempt, LinkedList<Integer> reachedTiles);

    void checkBoatFloating(Boat boat, int position);

    void printGrid();

    // parent activity will implement this method to respond to click events
    interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
