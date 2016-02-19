package com.mapps.mapp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mapps.mapp.drawing.Controller;

/**
 * Created by Ashot on 2/19/16.
 */
public class DrawingView extends View {

    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context context;
    private Paint mPaint;
    private float mX, mY;
    private static final float TOLERANCE = 3;
    private Controller controller;

    public DrawingView(Context context) {
        super(context);
        controller = new Controller(this, TOLERANCE);
    }



    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;

        // we set a new Path
        mPath = new Path();

        // and we set a new Paint with the desired attributes
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(4f);
        controller = new Controller(this, TOLERANCE);
    }

            // override onSizeChanged
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // your Canvas will draw onto the defined Bitmap
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

            // override onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // draw the mPath with the mPaint on the canvas when onDraw
        canvas.drawBitmap(mBitmap, 0, 0, null);
        controller.onDraw(canvas);
//        canvas.drawPath(mPath, mPaint);
    }

            // when ACTION_DOWN start touch according to the x,y values
    private void startTouch(float x, float y) {
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

            // when ACTION_MOVE move touch according to the x,y values
    private void moveTouch(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOLERANCE || dy >= TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

            // when ACTION_UP stop touch
    private void upTouch() {
        mPath.lineTo(mX, mY);
    }

            //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startTouch(x, y);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveTouch(x, y);
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP:
//                upTouch();
//                invalidate();
//                break;
//        }

        controller.onTouch(event);
        return true;
    }

    public void setBrush() {
        controller.setEraser(false);
    }

    public void setEraser() {
        controller.setEraser(true);
    }

    public Canvas getCanvas() {
        return mCanvas;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.WHITE);
        Canvas c = new Canvas(bitmap);
        c.drawBitmap(mBitmap, 0, 0, null);
        return bitmap;
    }

}
