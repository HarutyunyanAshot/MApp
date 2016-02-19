package com.mapps.mapp.drawing;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Ashot on 2/19/16.
 */
public class History {
    private final int MAX_STEPS_COUNT = 5;
    private  ArrayList<Bitmap> history;

    private int current = -1;

    public History() {
        history = new ArrayList<>();
    }

    public void push(Bitmap bitmap) {
        if (current == MAX_STEPS_COUNT) {
            history.remove(0).recycle();
        } else {
            for (int i =  history.size() - ++current; i > 0; i--) {
                history.remove(current).recycle();
            }

        }
        history.add(current, bitmap);

    }

    public Bitmap redo() {
        if (history.size() > 0 && current < history.size() - 1) {
            return history.get(++current).copy(Bitmap.Config.ARGB_8888, true);
        }
        return null;
    }

    public Bitmap undo() {
        if (history.size() > 0 && current > 0) {
            return history.get(--current).copy(Bitmap.Config.ARGB_8888, true);
        }
        return null;
    }

    public boolean canUndo() {
        return current > 0;
    }

    public boolean canRedo() {
        return current < history.size() - 1;
    }
}
