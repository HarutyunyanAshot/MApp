package com.mapps.mapp.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Ashot on 2/19/16.
 */
public class Brush extends Curve {
    private Paint paint;
    private Path path;

    // Last three points of the path
    private float[] lastX;
    private float[] lastY;

    private ArrayList<Float> points;

    public Brush() {
        super(false);

        path = new Path();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(6f);

        lastX = new float[3];
        lastY = new float[3];
        tmpBoundsF = new RectF();
        points = new ArrayList<>();
    }

    protected void addPoint(float x, float y, float pressure) {
        points.add(x);
        points.add(y);
        path.quadTo(lastX[2], lastY[2], (x + lastX[2]) / 2, (y + lastY[2]) / 2);

        lastX[0] = lastX[1];
        lastX[1] = lastX[2];
        lastX[2] = x;

        lastY[0] = lastY[1];
        lastY[1] = lastY[2];
        lastY[2] = y;

    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawPath(path, paint);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void setStartPoint(float x, float y, float pressure) {
        points.add(x);
        points.add(y);
        path.moveTo(x, y);

        lastX[0] = lastX[1] = lastX[2] = x;
        lastY[0] = lastY[1] = lastY[2] = y;
    }

    protected void update(Canvas canvas) {
        canvas.drawPath(path, paint);
        if (path.isEmpty())
            canvas.drawPoint(lastX[0], lastY[0], paint);
    }

    RectF tmpBoundsF;

    protected void computeUpdateBounds(Rect bounds) {
        float thickness = paint.getStrokeWidth();
//        if (paint.getMaskFilter() == Graphics.BLUR_MASK_FILTER)
//            thickness += Graphics.BLUR_RADIUS;

        tmpBoundsF.set(lastX[0], lastY[0], lastX[0], lastY[0]);
        tmpBoundsF.union(lastX[1], lastY[1]);
        tmpBoundsF.union(lastX[2], lastY[2]);
        tmpBoundsF.inset(-thickness, -thickness);
        tmpBoundsF.roundOut(bounds);
    }

}
