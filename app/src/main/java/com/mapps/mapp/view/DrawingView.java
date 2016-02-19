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
import com.mapps.mapp.drawing.History;

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
    private History hiatory;

    private OnChangeListener listener;

    public DrawingView(Context context) {
        super(context);
        hiatory = new History();
        controller = new Controller(this, TOLERANCE);
    }



    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        hiatory = new History();

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
        onUpdate();
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


    public void clearCanvas() {
        mPath.reset();
        invalidate();
    }

 public void undo() {
     Bitmap bitmap = hiatory.undo();
     if (bitmap != null) {
         mBitmap = bitmap;
         mCanvas.setBitmap(mBitmap);
         invalidate();
     }
 }

    public void redo() {
        Bitmap bitmap = hiatory.redo();
        if (bitmap != null) {
            mBitmap = bitmap;
            mCanvas.setBitmap(mBitmap);
            invalidate();
        }
    }

    public boolean canUndo() {
        return hiatory.canUndo();
    }

    public boolean canRedo() {
        return hiatory.canRedo();
    }

    public void onUpdate() {
        hiatory.push(mBitmap.copy(Bitmap.Config.ARGB_8888, true));
        if (listener != null) {
            listener.onChange();
        }
    }

            //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {

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

    public void setOnChangeListener(OnChangeListener listener) {
        this.listener = listener;
    }

   public interface OnChangeListener {
        void onChange();
    }

}
