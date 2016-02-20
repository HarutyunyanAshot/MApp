package com.mapps.mapp.items;

import java.io.Serializable;

/**
 * Created by Ashot on 2/20/16.
 */
public class BgItem implements Serializable {
    private int size;
    private int color;
    private String path;

    public int getSize() {
        return size;
    }

    public int getColor() {
        return color;
    }

    public String getPath() {
        return path;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
