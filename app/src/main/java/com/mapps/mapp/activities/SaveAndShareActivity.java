package com.mapps.mapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;
import com.mapps.mapp.utils.PhotoUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by Mariam on 2/19/16.
 */
public class SaveAndShareActivity extends MAppBaseActivity {
    boolean isSaved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_and_share);
        Intent intent = getIntent();
        final String path = intent.getStringExtra(DrawingActivity.EXTRA_PREVIEW_PATH);
        ImageView previewImage = (ImageView) findViewById(R.id.preview_image);
        Glide.with(this).asBitmap().load(path).into(previewImage);
        final Animation blinkAnimation = new ScaleAnimation(1,1.2f,1,1.2f);
        blinkAnimation.setDuration(500); // duration - half a second
        blinkAnimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkAnimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        blinkAnimation.setRepeatMode(Animation.REVERSE); // Reverse animation at the end so the button
        final ImageButton btnSave = (ImageButton)findViewById(R.id.btn_save);
        btnSave.startAnimation(blinkAnimation);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToGallery(path);
                btnSave.clearAnimation();
            }
        });
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    shareMedia(path);
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.btn_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SaveAndShareActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                finish();
            }
        });

    }

    public void shareMedia(String filePath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        File media = new File(filePath);
        Uri uri = Uri.fromFile(media);
        try {
            share.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(share, "Share with"));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    private void saveToGallery(String filePath) {
        String newFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + new File(filePath).getName();
        try {
            PhotoUtils.copyDirectoryOneLocationToAnotherLocation(new File(filePath), new File(newFilePath));
            Toast.makeText(this, "Image successfully saved.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
