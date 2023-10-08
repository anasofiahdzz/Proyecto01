package com.sodiri.tamarweather.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sodiri.tamarweather.R;

import java.util.Timer;
import java.util.TimerTask;

public class ScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        TimerTask screen = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(ScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer time = new Timer();
        time.schedule(screen, 8000);
    }
}