package com.mapps.mapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;

import java.io.File;

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

        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.btn_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSaved) {
                    shareMedia(path);
                }
            }
        });
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
}
