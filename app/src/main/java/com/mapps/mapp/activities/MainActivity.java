package com.mapps.mapp.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mapps.mapp.R;

public class MainActivity extends MAppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView drawItemList = (RecyclerView)findViewById(R.id.draw_item_list);
        int columnCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,columnCount);
    }
}
