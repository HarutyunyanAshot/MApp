package com.mapps.mapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mapps.mapp.R;
import com.mapps.mapp.items.DrawItem;

/**
 * Created by Mariam on 2/19/16.
 */
public class DrawItemListAdapter extends RecyclerViewAdapter<DrawItem, DrawItemListAdapter.DrawItemViewHolder> {
    private Context context;

    public DrawItemListAdapter(Context context) {
        super(context);
        this.context = context;
    }


    @Override
    public DrawItemViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new DrawItemViewHolder(LayoutInflater.from(context).inflate(R.layout.draw_item,parent,false));

    }

    @Override
    public void onBindViewHolder(DrawItemViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Glide.with(context)
                .load(Uri.parse("file:///android_asset/" + itemsList.get(position).imagePath))
                .into(holder.image);
    }

    class DrawItemViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        FrameLayout itemFrame;
        public DrawItemViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            itemFrame = (FrameLayout)itemView.findViewById(R.id.item_frame);

        }
    }
}
