package com.mapps.mapp.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;
import com.mapps.mapp.view.DrawingView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawingActivity extends  MAppBaseActivity{
    private DrawingView drawingView;
    private ImageView bgImage;
    private Button prev;
    private Button next;
    private ArrayList<String> imageDetailsPaths;
    private int currentImageIndex = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draving);

        Intent intent = getIntent();
        String previewImagePath = intent.getStringExtra(MainActivity.EXTRA_PREVIEW_IMAGE_PATH);
        imageDetailsPaths = intent.getStringArrayListExtra(MainActivity.EXTRA_IMAGE_DETAILS_PATHS);

        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        bgImage = (ImageView) findViewById(R.id.bg_image);
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

        Glide.with(this).asBitmap().load(Uri.parse("file:///android_asset/" + previewImagePath)).into(bgImage);
        initButtons();

    }

    private void initButtons() {
        prev = (Button) findViewById(R.id.btn_prev);
        next = (Button) findViewById(R.id.btn_next);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex == imageDetailsPaths.size()) {
                    bgImage.setAlpha(0.7f);
                    currentImageIndex--;
                } else if (currentImageIndex > 0) {
                    try {

                        bgImage.setImageDrawable(Drawable.createFromStream(getAssets().open(imageDetailsPaths.get(--currentImageIndex)), null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < imageDetailsPaths.size() - 1) {
                    try {
                        bgImage.setAlpha(0.7f);
                        bgImage.setImageDrawable(Drawable.createFromStream(getAssets().open(imageDetailsPaths.get(++currentImageIndex)), null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (currentImageIndex == imageDetailsPaths.size() - 1) {
                    bgImage.setAlpha(0f);
                    currentImageIndex++;

                }
            }
        });

    }
}
