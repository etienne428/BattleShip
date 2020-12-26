package com.example.battleShip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.example.battleShip.model.GameActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_COLUMNS =
            "com.example.android.battleShip.extra.COLUMNS";
    public static final String EXTRA_ROWS =
            "com.example.android.battleShip.extra.ROWS";

    private int columns;
    private int rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        columns = 10;
        rows = 12;
        Button launchGame = findViewById(R.id.launch_game);
        launchGame.setOnClickListener( o -> {
            Intent intent = new Intent(this,
                    GameActivity.class);
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
}