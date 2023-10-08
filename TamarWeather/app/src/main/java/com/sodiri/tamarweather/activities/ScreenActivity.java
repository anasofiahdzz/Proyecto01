package com.sodiri.tamarweather.activities;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import androidx.appcompat.app.AppCompatActivity;
import com.sodiri.tamarweather.R;
import java.util.TimerTask;
import java.util.Timer;
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