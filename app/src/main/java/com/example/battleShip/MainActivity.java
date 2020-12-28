package com.example.battleShip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.battleShip.model.GameActivity;
import com.example.battleShip.model.SetBoatsActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_COLUMNS =
            "com.example.android.battleShip.extra.COLUMNS";
    public static final String EXTRA_ROWS =
            "com.example.android.battleShip.extra.ROWS";
    public static final String BOAT_SETUP =
            "com.example.android.battleShip.extra.SETUP";

    private int columns;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        columns = 10;
        rows = 12;
        if (savedInstanceState != null) {
            columns = savedInstanceState.getInt(EXTRA_COLUMNS);
            rows = savedInstanceState.getInt(EXTRA_ROWS);
        }

        Button beginGame = findViewById(R.id.begin_game);
        beginGame.setOnClickListener( o -> {
            Intent intent = new Intent(this,
                    SetBoatsActivity.class);
            intent.putExtra(EXTRA_COLUMNS, columns);
            intent.putExtra(EXTRA_ROWS, rows);
            startActivity(intent);
        });
        Button launchGame = findViewById(R.id.launch_game);
        launchGame.setOnClickListener( o -> {

            Intent intent = new Intent(this,
                    GameActivity.class);
            Log.e("NULL", "columns = " + columns + ", rows = " + rows);
            intent.putExtra(EXTRA_COLUMNS, columns);
            intent.putExtra(EXTRA_ROWS, rows);
            startActivity(intent);
        });
    }

    public void chooseLanguage(View view) {
        Intent languageIntent = new
                Intent(Settings.ACTION_LOCALE_SETTINGS);
        startActivity(languageIntent);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the state of the fragment (true=open, false=closed).
        savedInstanceState.putInt(EXTRA_COLUMNS, columns);
        savedInstanceState.putInt(EXTRA_ROWS, rows);
        super.onSaveInstanceState(savedInstanceState);
    }
}