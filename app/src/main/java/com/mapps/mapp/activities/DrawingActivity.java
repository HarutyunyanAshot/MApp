package com.mapps.mapp.activities;

import android.os.Bundle;
import android.view.View;

import com.mapps.mapp.R;
import com.mapps.mapp.view.DrawingView;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawingActivity extends  MAppBaseActivity{
    private DrawingView drawingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draving);
        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        View btnBrush = findViewById(R.id.btn_brush);
        View btnEraser = findViewById(R.id.btn_eraser);
        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setBrush();
            }
        });

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraser();
            }
        });
    }
}
