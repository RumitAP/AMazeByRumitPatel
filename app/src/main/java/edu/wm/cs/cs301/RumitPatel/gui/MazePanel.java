package edu.wm.cs.cs301.RumitPatel.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.security.Key;

public class MazePanel extends View implements P5PanelF21 {
    private Paint g;
    private Path path = new Path();
    private Canvas canvas;
    private Bitmap bitmap;
    private int rgb;

    public MazePanel(Context context) {
        super(context);
        setFocusable(false);
        g = new Paint();
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        commit();

    }

    public MazePanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        g = new Paint();
        bitmap = Bitmap.createBitmap(Constants.VIEW_WIDTH, Constants.VIEW_HEIGHT, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        commit();
    }

    public void testImage(Canvas canvas) {
        this.canvas = canvas;
        g.setColor(Color.RED);
        addFilledRectangle(300, 300, 150, 150);
        commit();
    }

    //private void update() {
    //    invalidate();
    //}

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, g);
        invalidate();
    }

    /**
     * https://stackoverflow.com/questions/7423082/authorative-way-to-override-onmeasure
     *
     * @param width  is the width
     * @param height is the height
     */
    @Override
    protected void onMeasure(int width, int height) {
        super.onMeasure(width, height);
        this.setMeasuredDimension(width, height);

    }


    @Override
    public void commit() {
        invalidate();
    }


    @Override
    public boolean isOperational() {
        return false;
    }

    @Override
    public void setColor(int rgb) {
        this.rgb = rgb;
        g.setColor(rgb);
        Log.v("MazePanel", "Set the color.");
    }

    @Override
    public int getColor() {
        return rgb;
    }

    @Override
    public void addBackground(float percentToExit) {
        g.setColor(blend(Color.parseColor("#BF40BF"), Color.parseColor("#000000"), percentToExit));
    }

    private int blend(int fstColor, int sndColor, double weightFstColor) {
        if (weightFstColor < 0.1)
            return sndColor;
        if (weightFstColor > 0.95)
            return fstColor;
        double r = weightFstColor * fstColor + (1 - weightFstColor) * sndColor;
        double g = weightFstColor * fstColor + (1 - weightFstColor) * sndColor;
        double b = weightFstColor * fstColor + (1 - weightFstColor) * sndColor;
        double a = Math.max(fstColor, sndColor);

        return Color.rgb((int) r, (int) g, (int) b);
    }

    /**
     * Used: https://developer.android.com/reference/android/graphics/Path
     * https://stackoverflow.com/questions/2047573/how-to-draw-filled-polygon/36792553
     *
     * @param x      is the x-coordinate of the top left corner
     * @param y      is the y-coordinate of the top left corner
     * @param width  is the width of the rectangle
     * @param height is the height of the rectangle
     */
    @Override
    public void addFilledRectangle(int x, int y, int width, int height) {
        Rect rect = new Rect(x, y, width + x, height + y);
        canvas.drawRect(rect, g);
    }

    /**
     * https://stackoverflow.com/questions/2047573/how-to-draw-filled-polygon/36792553
     * Used: https://developer.android.com/reference/android/graphics/Path
     *
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addFilledPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        int i = 1;
        while (i < nPoints) {
            path.lineTo(xPoints[i], yPoints[i]); // should it both be the same? TODO
            Log.v("MazePanel", "drawing a polygon");
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, g);

    }

    /**
     * Used: https://developer.android.com/reference/android/graphics/Path
     * https://stackoverflow.com/questions/2047573/how-to-draw-filled-polygon/36792553
     *
     * @param xPoints are the x-coordinates of points for the polygon
     * @param yPoints are the y-coordinates of points for the polygon
     * @param nPoints is the number of points, the length of the arrays
     */
    @Override
    public void addPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        path.reset();
        path.moveTo(xPoints[0], yPoints[0]);
        int i = 1;
        while (i < nPoints) {
            path.lineTo(xPoints[i], yPoints[i]); // should it both be the same? TODO
            Log.v("MazePanel", "drawing a polygon");
        }
        path.lineTo(xPoints[0], yPoints[0]);
        canvas.drawPath(path, g);

    }

    @Override
    public void addLine(int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX, startY, endX, endY, g);
    }

    @Override
    public void addFilledOval(int x, int y, int width, int height) {
        canvas.drawOval(x, y, width, height, g);
    }

    @Override
    public void addArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        canvas.drawArc(x, y, width, height, startAngle, arcAngle, false, g);
    }

    @Override
    public void addMarker(float x, float y, String str) {
        canvas.drawText(str, x, y, g);
        Log.v("MazePanel", "just did addMarker");
    }

    @Override
    public void setRenderingHint(P5RenderingHints hintKey, P5RenderingHints hintValue) {

    }
}
