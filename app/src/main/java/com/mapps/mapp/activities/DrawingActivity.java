package com.mapps.mapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;
import com.mapps.mapp.items.BgItem;
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
    private ArrayList<BgItem> bgItems;
    private int currentImageIndex = -1;
    private boolean isTouchEnabled = false;
    public static final String DRAWING_FOLDER = Environment.getExternalStorageDirectory() + "/" + ".drawings";
    private String imageName;
    public static final String EXTRA_PREVIEW_PATH = "previewPath";
    private String previewImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draving);

        Intent intent = getIntent();
        previewImagePath = intent.getStringExtra(MainActivity.EXTRA_PREVIEW_IMAGE_PATH);
        bgItems = (ArrayList<BgItem>) intent.getSerializableExtra(MainActivity.EXTRA_BACKGROUND_ITEMS);
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
        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isTouchEnabled)
                    drawingView.onTouchEvent(event);
                else {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        next.callOnClick();
                        isTouchEnabled = true;
                    }
                }
                return true;
            }
        });

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
        final ImageButton btnDone = (ImageButton)findViewById(R.id.btn_done);
        final Animation blinkAnimation = new ScaleAnimation(1,1.2f,1,1.2f);
        blinkAnimation.setDuration(500); // duration - half a second
        blinkAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkAnimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        blinkAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button will fade back in
        next.startAnimation(blinkAnimation);
        prev.setEnabled(false);
        prev.setAlpha(0.6f);

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
                if (currentImageIndex == bgItems.size()) {
                    next.setEnabled(true);
                    next.setAlpha(1f);
                    bgImage.setAlpha(0.5f);
                    currentImageIndex--;
                } else if (currentImageIndex > 0) {
                    try {
                        currentImageIndex--;
                        switch (bgItems.get(currentImageIndex).getSize()) {
                            case 1:
                                drawingView.setBrushSize(6f);
                                break;
                            case 2:
                                drawingView.setBrushSize(15f);
                                break;
                        }
                        drawingView.setBrushColor(bgItems.get(currentImageIndex).getColor());
                        bgImage.setImageDrawable(Drawable.createFromStream(getAssets().open(bgItems.get(currentImageIndex).getPath()), null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        bgImage.setImageDrawable(Drawable.createFromStream(getAssets().open(previewImagePath), null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isTouchEnabled = false;
                    currentImageIndex--;
                    prev.setEnabled(false);
                    prev.setAlpha(0.5f);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentImageIndex < bgItems.size() - 1) {
                    try {
                        currentImageIndex++;
                        switch (bgItems.get(currentImageIndex).getSize()) {
                            case 1:
                                drawingView.setBrushSize(8f);
                                break;
                            case 2:
                                drawingView.setBrushSize(20f);
                                break;
                        }
                        drawingView.setBrushColor(bgItems.get(currentImageIndex).getColor());
                        bgImage.setAlpha(0.5f);
                        prev.setEnabled(true);
                        prev.setAlpha(1f);
                        isTouchEnabled = true;
                        bgImage.setImageDrawable(Drawable.createFromStream(getAssets().open(bgItems.get(currentImageIndex).getPath()), null));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (currentImageIndex == bgItems.size() - 1) {
                    next.setEnabled(false);
                    next.setAlpha(0.5f);
                    next.clearAnimation();
                    btnDone.startAnimation(blinkAnimation);
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
        btnDone.setOnClickListener(new View.OnClickListener() {
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
