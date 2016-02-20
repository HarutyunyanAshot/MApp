package com.mapps.mapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.mapps.mapp.R;

/**
 * Created by Ashot on 2/20/16.
 */
public class SplashScreenActivity extends MAppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final long delay = 2000;
        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
