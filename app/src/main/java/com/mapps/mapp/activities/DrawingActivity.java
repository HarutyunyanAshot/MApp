package com.mapps.mapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;
import com.mapps.mapp.utils.PhotoUtils;
import com.mapps.mapp.view.DrawingView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawingActivity extends MAppBaseActivity {
    private DrawingView drawingView;
    private ImageView bgImage;
    private ImageButton prev;
    private ImageButton next;
    private ImageButton undo;
    private ImageButton redo;
    private ArrayList<String> imageDetailsPaths;
    private int currentImageIndex = -1;
    public static final String DRAWING_FOLDER = Environment.getExternalStorageDirectory() + "/" + "drawings";
    private String imageName;
    public static final String EXTRA_PREVIEW_PATH = "previewPath";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draving);

        Intent intent = getIntent();
        String previewImagePath = intent.getStringExtra(MainActivity.EXTRA_PREVIEW_IMAGE_PATH);
        imageDetailsPaths = intent.getStringArrayListExtra(MainActivity.EXTRA_IMAGE_DETAILS_PATHS);
        imageName = intent.getStringExtra(MainActivity.EXTRA_IMAGE_NAME);
        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        bgImage = (ImageView) findViewById(R.id.bg_image);
        final View btnBrush = findViewById(R.id.btn_brush);
        final View btnEraser = findViewById(R.id.btn_eraser);
        btnBrush.setSelected(true);
        btnBrush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnEraser.setSelected(false);
                btnBrush.setSelected(true);
                drawingView.setBrush();
            }
        });

        btnEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.setEraser();
                btnEraser.setSelected(true);
                btnBrush.setSelected(false);
            }
        });

        Glide.with(this).asBitmap().load(Uri.parse("file:///android_asset/" + previewImagePath)).into(bgImage);
        initButtons();

        drawingView.setOnChangeListener(new DrawingView.OnChangeListener() {
            @Override
            public void onChange() {
                undo.setEnabled(drawingView.canUndo());
                redo.setEnabled(drawingView.canRedo());
            }
        });

    }

    private void initButtons() {
        prev = (ImageButton) findViewById(R.id.btn_prev);
        next = (ImageButton) findViewById(R.id.btn_next);
        undo = (ImageButton) findViewById(R.id.btn_undo);
        redo = (ImageButton) findViewById(R.id.btn_redo);

        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.undo();
                undo.setEnabled(drawingView.canUndo());
                redo.setEnabled(drawingView.canRedo());
            }
        });

        redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.redo();
                undo.setEnabled(drawingView.canUndo());
                redo.setEnabled(drawingView.canRedo());
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex == imageDetailsPaths.size()) {
                    bgImage.setAlpha(0.6f);
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
                        bgImage.setAlpha(0.6f);
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
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = imageName + "_" + System.currentTimeMillis() + ".jpg";
                PhotoUtils.saveBitmapToDir(DRAWING_FOLDER, name, drawingView.getBitmap(), DrawingActivity.this, Bitmap.CompressFormat.JPEG);
                Intent intent = new Intent(DrawingActivity.this, SaveAndShareActivity.class);
                intent.putExtra(EXTRA_PREVIEW_PATH, DRAWING_FOLDER + "/" + name);
                startActivity(intent);
            }
        });

    }

}
