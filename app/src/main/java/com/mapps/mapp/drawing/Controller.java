package com.mapps.mapp.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.mapps.mapp.view.DrawingView;

/**
 * Created by Ashot on 2/19/16.
 */
public class Controller {

    private DrawingView drawingView;
    private final float touchTollerance;


    private float prevX;
    private float prevY;

    private int color = Color.BLACK;
    private float size = 6f;

    private boolean isEraser = false;

    private Curve curve;

    public Controller(DrawingView view, float touchTollerance) {

        assert touchTollerance > 0;
        drawingView = view;
        this.touchTollerance = touchTollerance;
        updateBounds = new Rect();
    }

    public final void onTouchEventReceived(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onTouchDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (distance(prevX, prevY, x, y) < touchTollerance)
                    return;
                onTouchMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                onTouchUp(x, y);
                break;
            case MotionEvent.ACTION_CANCEL:
                onTouchCancel(x, y);
                break;
            default:
                assert false;
        }
        prevX = x;
        prevY = y;
    }

    private Rect updateBounds;

    protected void onTouchDown(float x, float y) {
        curve = isEraser ? new Eraser() : new Brush();
        curve.setBrushColor(color);
        curve.setBrushSize(size);
        curve.setStartPoint(x, y, 0.0f);//TODO revise
        if (curve.isPiecewiseUpdatable())
            curve.update(drawingView.getCanvas());
        curve.computeUpdateBounds(updateBounds);
        invalidateDrawingView(updateBounds);
    }

    protected void onTouchMove(float x, float y) {
        curve.addPoint(x, y, 0.0f);
        if (curve.isPiecewiseUpdatable())
            curve.update(drawingView.getCanvas());
        curve.computeUpdateBounds(updateBounds);
        invalidateDrawingView(updateBounds);
    }

    protected void onTouchUp(float x, float y) {
        if (!curve.isPiecewiseUpdatable()) {
            curve.update(drawingView.getCanvas());
            curve.computeUpdateBounds(updateBounds);
            invalidateDrawingView(updateBounds);
        }
        drawingView.onUpdate();
        curve = null;
    }

    protected void onTouchCancel(float x, float y) {
        Log.d("SimpleDrawingController", "OnTouchCancel");
    }


    public void onDraw(Canvas canvas) {
        if (curve != null && !curve.isPiecewiseUpdatable())
            curve.update(canvas);
    }


    protected final void invalidateDrawingView(Rect dirty) {
        if (!dirty.isEmpty())
            drawingView.invalidate(dirty);
    }

    public float distance(float x1, float y1, float x2, float y2) {
        float dx = x1 - x2;
        float dy = y1 - y2;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return dist;
    }

    public boolean onTouch(MotionEvent event) {
        if (drawingView.getCanvas() == null)
            return false;


        onTouchEventReceived(event);
        return true;
    }

    public void setBrushSize(float size) {
        this.size = size;
    }

    public void setBrushColor(int color) {
        this.color = color;
    }

    public void setEraser(boolean isEraser) {
        this.isEraser = isEraser;
    }

}
