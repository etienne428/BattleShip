package com.example.battleShip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.battleShip.model.GameActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_COLUMNS =
            "com.example.android.droidcafeinput.extra.COLUMNS";
    public static final String EXTRA_ROWS =
            "com.example.android.droidcafeinput.extra.ROWS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button launchGame = findViewById(R.id.launch_game);
        launchGame.setOnClickListener( o -> {
            Intent intent = new Intent(this,
                    GameActivity.class);
            intent.putExtra(EXTRA_COLUMNS, "10");
            intent.putExtra(EXTRA_ROWS, "12");
            startActivity(intent);
        });
    }
}