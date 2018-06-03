package com.base.baselibrary.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.base.baselibrary.util.ScreenUtils;

import static android.graphics.Bitmap.Config;
import static android.graphics.Bitmap.createBitmap;
import static android.graphics.Bitmap.createScaledBitmap;

/**
 * Created by 15600 on 2017/7/11.
 */

public class ImageViewCircle extends android.support.v7.widget.AppCompatImageView {

    private Paint paint = new Paint();

    //基本的三个构造函数
    public ImageViewCircle(Context context) {
        super(context);
    }

    public ImageViewCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {

    }

    //自定义View实现过程中很重要的onDraw绘制图形的方法
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable mDrawable = getDrawable();
        if (mDrawable == null) return;
        if (mDrawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) mDrawable).getBitmap();
//            //核心代码
//            Bitmap b = getCircleBitmap(bitmap);
//            Rect res=new Rect(0,0,b.getWidth(),b.getHeight());
//            Rect dst=new Rect(0,0,getMeasuredWidth(),getMeasuredHeight());
//            paint.reset();
//            canvas.drawBitmap(b,res,dst,null);


            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            Bitmap target = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Config
                    .ARGB_8888);
            Canvas canvas1 = new Canvas(target);
            RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
            int round=ScreenUtils.dip2px(8);
            canvas1.drawRoundRect(rect, round, round, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas1.drawBitmap(bitmap, 0, 0, paint);


            canvas.drawBitmap(target, 0, 0, null);

        } else {
            super.onDraw(canvas);
        }

    }


//    /**
//     * 绘制
//     */
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//
//        switch (type)
//        {
//            // 如果是TYPE_CIRCLE绘制圆形
//            case TYPE_CIRCLE:
//
//                int min = Math.min(mWidth, mHeight);
//                /**
//                 * 长度如果不一致，按小的值进行压缩
//                 */
//                mSrc = Bitmap.createScaledBitmap(mSrc, min, min, false);
//
//                canvas.drawBitmap(createCircleImage(mSrc, min), 0, 0, null);
//                break;
//            case TYPE_ROUND:
//                canvas.drawBitmap(createRoundConerImage(mSrc), 0, 0, null);
//                break;
//
//        }
//
//    }

    /**
     * 根据原图和变长绘制圆形图片
     *
     * @param source
     * @param min
     * @return
     */
    private Bitmap createCircleImage(Bitmap source, int min) {
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(min, min, Config.ARGB_8888);
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(min / 2, min / 2, min / 2, paint);
        /**
         * 使用SRC_IN，参考上面的说明
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

//    /**
//     * 根据原图添加圆角
//     *
//     * @param source
//     * @return
//     */
//    private Bitmap createRoundConerImage(Bitmap source)
//    {
//        final Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Config.ARGB_8888);
//        Canvas canvas = new Canvas(target);
//        RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());
//        canvas.drawRoundRect(rect, mRadius, mRadius, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(source, 0, 0, paint);
//        return target;
//    }


    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getHeight() / 2,
                paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);//将图片画出来
        return output;
    }


    /**
     * 初始Bitmap对象的缩放裁剪过程
     *
     * @param bmp    初始Bitmap对象
     * @param radius 圆形图片直径大小
     * @return 返回一个圆形的缩放裁剪过后的Bitmap对象
     */
    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        //比较初始Bitmap宽高和给定的圆形直径，判断是否需要缩放裁剪Bitmap对象
        if (bmp.getWidth() != radius || bmp.getHeight() != radius)
            sbmp = createScaledBitmap(bmp, radius, radius, false);
        else
            sbmp = bmp;
        Bitmap output = createBitmap(sbmp.getWidth(), sbmp.getHeight(),
                Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
                sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
        //核心部分，设置两张图片的相交模式，在这里就是上面绘制的Circle和下面绘制的Bitmap
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);

        return output;
    }

}

