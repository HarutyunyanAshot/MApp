package com.mapps.mapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mapps.mapp.R;
import com.mapps.mapp.adapters.DrawItemListAdapter;
import com.mapps.mapp.items.BgItem;
import com.mapps.mapp.items.DrawItem;
import com.mapps.mapp.listeners.RecyclerItemClickListener;
import com.mapps.mapp.utils.JsonUtils;

import java.util.ArrayList;

public class MainActivity extends MAppBaseActivity {
    private DrawItemListAdapter adapter;
    public static final String EXTRA_BACKGROUND_ITEMS = "bgItems";
    public static final String EXTRA_PREVIEW_IMAGE_PATH = "previewImagePath";
    public static final String EXTRA_IMAGE_NAME = "ImageName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView drawItemList = (RecyclerView)findViewById(R.id.draw_item_list);
        int columnCount = 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,columnCount);
        drawItemList.setLayoutManager(gridLayoutManager);
        adapter = new DrawItemListAdapter(this);
        ArrayList<DrawItem>  drawItems = JsonUtils.getCategories(getResources(),"meta.json");
        adapter.addAll(drawItems);
        drawItemList.setAdapter(adapter);
        drawItemList.addOnItemTouchListener(new RecyclerItemClickListener(MainActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,DrawingActivity.class);
                ArrayList<BgItem> bgItems = adapter.getItem(position).bgItems;
                intent.putExtra(EXTRA_BACKGROUND_ITEMS,bgItems);
                intent.putExtra(EXTRA_PREVIEW_IMAGE_PATH, adapter.getItem(position).imagePath);
                intent.putExtra(EXTRA_IMAGE_NAME,adapter.getItem(position).itemName);
                startActivity(intent);
            }
        }));

    }

}
