package com.base.baselibrary.view.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.base.baselibrary.util.ScreenUtils;


/**
 * Created by langlang on 2016/8/15.
 */

    public class BackDrawable extends Drawable {

    private Paint paint;

    private Path path;

    private int whSize;

    public BackDrawable() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        whSize = ScreenUtils.dip2px(30);////宽高大小
        path = new Path();
    }

    @Override
    public void draw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        float lineWidth = whSize * 0.5f;
        float lineHeight = lineWidth * 0.12f;
        canvas.save();
        canvas.rotate(-45, 0, lineWidth);
        path.moveTo(0, lineWidth);
        path.lineTo(0, lineWidth + lineHeight);
        path.lineTo(lineWidth, lineWidth + lineHeight);
        path.lineTo(lineWidth, lineWidth);
        path.lineTo(0, lineWidth);
        canvas.drawPath(path, paint);
        canvas.restore();
        path.reset();
        path.moveTo(0, lineWidth);
        path.lineTo(0, lineWidth - lineHeight);
        path.lineTo(lineWidth, lineWidth - lineHeight);
        path.lineTo(lineWidth, lineWidth);
        path.lineTo(0, lineWidth);
        canvas.rotate(45, 0, lineWidth);
        canvas.drawPath(path, paint);
        path.reset();
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getIntrinsicHeight() {
        return whSize;
    }

    @Override
    public int getIntrinsicWidth() {
        return whSize;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }
}
