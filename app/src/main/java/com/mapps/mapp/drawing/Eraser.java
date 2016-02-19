package com.mapps.mapp.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;

/**
 * Created by Ashot on 2/19/16.
 */
public class Eraser extends Curve {

    private ArrayList<Float> points;
    private Paint paint;
    private Path path;
    private Path tempPath;

    public Eraser() {
        super(true);

        path = new Path();
        tempPath = new Path();

        points = new ArrayList<Float>();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.TRANSPARENT);// TODO test
        paint.setStrokeWidth(4f);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
    }

    protected void addPoint(float x, float y, float pressure) {
        points.add(x);
        points.add(y);
    }

    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);

    }

    protected void setStartPoint(float x, float y, float pressure) {
        points.add(x);
        points.add(y);
        path.moveTo(x, y);
    }

    protected void update(Canvas canvas) {
        if (points.size() < 4)
            return;
        if (points.size() == 4) {
            float x0 = points.get(0);
            float y0 = points.get(1);
            float x1 = points.get(2);
            float y1 = points.get(3);
            float mx = (x0 + x1) / 2;
            float my = (y0 + y1) / 2;
            canvas.drawLine(x0, y0, mx, my, paint);
        } else {

            float x0 = points.get(points.size() - 6);
            float y0 = points.get(points.size() - 5);
            float x1 = points.get(points.size() - 4);
            float y1 = points.get(points.size() - 3);
            float x2 = points.get(points.size() - 2);
            float y2 = points.get(points.size() - 1);

            float mx0 = (x0 + x1) / 2;
            float my0 = (y0 + y1) / 2;
            float mx1 = (x1 + x2) / 2;
            float my1 = (y1 + y2) / 2;

            tempPath.rewind();
            tempPath.moveTo(mx0, my0);
            tempPath.quadTo(x1, y1, mx1, my1);
            path.quadTo(x1, y1, mx1, my1);
            canvas.drawPath(tempPath, paint);
        }
    }

    private RectF tempBoundsF = new RectF();

    protected void computeUpdateBounds(Rect updateBounds) {
        int thickness = (int) paint.getStrokeWidth() + 1;

        if (points.isEmpty()) {
            updateBounds.setEmpty();
            return;
        } else if (points.size() < 6) {
            updateBounds.left = points.get(0).intValue();
            updateBounds.top = points.get(1).intValue();
            updateBounds.right = points.get(0).intValue();
            updateBounds.bottom = points.get(1).intValue();
            if (points.size() == 4)
                addPointToRect(updateBounds, points.get(2), points.get(3));
            updateBounds.inset(-thickness, -thickness);
            return;
        }

        path.computeBounds(tempBoundsF, false);
        tempBoundsF.roundOut(updateBounds);
        updateBounds.inset(-thickness, -thickness);
    }

    public void addPointToRect(Rect rect, float x, float y) {
        rect.left = Math.min(rect.left, (int) x - 1);
        rect.top = Math.min(rect.top, (int) y - 1);
        rect.right = Math.max(rect.right, (int) x + 1);
        rect.bottom = Math.max(rect.bottom, (int) y + 1);
    }

}
