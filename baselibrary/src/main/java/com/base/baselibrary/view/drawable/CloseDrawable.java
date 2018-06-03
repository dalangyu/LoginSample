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

    public class CloseDrawable extends Drawable {

    private Paint paint;

    private Path path;

    private int whSize;

    public CloseDrawable() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        whSize = ScreenUtils.dip2px(30);////宽高大小
        path = new Path();
    }

    @Override
    public void draw(Canvas canvas) {
        float half=whSize *0.5f;//绘制区域的一半
        float lineHeight=half*0.12f;//箭头高
        paint.setStyle(Paint.Style.FILL);
        canvas.rotate(45,half,half+lineHeight*0.5f);
        path.addRect(0,half,whSize,half+lineHeight, Path.Direction.CW);
        canvas.drawPath(path,paint);
        path.reset();
        canvas.rotate(-90,half,half+lineHeight*0.5f);
        path.addRect(0,half,whSize,half+lineHeight, Path.Direction.CW);
        canvas.drawPath(path,paint);
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
