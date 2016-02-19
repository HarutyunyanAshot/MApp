package com.mapps.mapp.drawing;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Ashot on 2/19/16.
 */
public abstract class Curve {

    private final boolean piecewiseUpdatable;

    protected Curve(boolean piecewiseUpdatable) {
        this.piecewiseUpdatable = piecewiseUpdatable;
    }

    protected final boolean isPiecewiseUpdatable() {
        return piecewiseUpdatable;
    }

    protected abstract void setStartPoint(float x, float y, float pressure);

    protected abstract void addPoint(float x, float y, float pressure);

//    public abstract void draw(Canvas canvas);

    protected abstract void update(Canvas canvas);

    protected abstract void computeUpdateBounds(Rect updateBounds);
}
