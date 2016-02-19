package com.mapps.mapp.controller;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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

    private Path mPath;
    private Paint mPaint;

    private float prevX;
    private float prevY;

    private boolean isEraser = false;

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
         init();
        mPath.moveTo(x, y);//TODO revise
        update();
        invalidateDrawingView();
    }

    protected void onTouchMove(float x, float y) {
        float dx = Math.abs(x - prevX);
        float dy = Math.abs(y - prevY);
        if (dx >= touchTollerance || dy >= touchTollerance) {
            mPath.quadTo(prevX, prevY, (x + prevX) / 2, (y + prevY) / 2);
            prevX = x;
            prevY = y;
        }
        update();
        invalidateDrawingView();
    }

    protected void onTouchUp(float x, float y) {
       update();
          invalidateDrawingView();
        mPath = null;
    }

    protected void onTouchCancel(float x, float y) {
        Log.d("SimpleDrawingController", "OnTouchCancel");
    }

//    @Override
//    protected void onDraw(Canvas paperViewCanvas) {
//        if (curve != null && !curve.isPiecewiseUpdatable())
//            curve.update(paperViewCanvas);
//    }

    private void update() {
        drawingView.getCanvas().drawPath(mPath, mPaint);
    }
    private void init() {
        mPath = new Path();

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setAntiAlias(true);
        if (isEraser) {
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        } else {
            mPaint.setColor(Color.BLACK);
        }
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);

//        mPaint.setAlpha(alpha);
    }

    private void invalidateDrawingView() {
        drawingView.invalidate();
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

    public void setEraser(boolean isEraser) {
        this.isEraser = isEraser;
    }
}
