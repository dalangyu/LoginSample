package com.base.baselibrary.util;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 图片工具类.
 *
 * @author tjt
 */
public class ImgUtils {

    /**
     * Bitmap转换为InputStream.
     *
     * @param bm Bitmap
     * @return
     */
    public static InputStream getBitmapTurnInputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        try {
            sbs.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sbs;
    }


    /**
     * 把URI数据结构转换为物理地址
     *
     * @param activity
     * @param uri
     * @return
     */
    public static String getUriTurnFilePath(Activity activity, Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null,
                null);
        int actual_image_column_index = actualimagecursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();
        return actualimagecursor.getString(actual_image_column_index);
    }

    /**
     * 把Bitmap写入到指定的文件路径上。
     *
     * @param bitmap 图片
     * @param path   文件路径
     * @return
     */
    public static String storeBitMapToFile(Bitmap bitmap, String path) {
        if (bitmap == null) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            File fileParent = file.getParentFile();
            if (null == fileParent) {
                file.mkdirs();
            }
            if (!fileParent.exists())
                fileParent.mkdirs();
        }
        RandomAccessFile accessFile = null;
        ByteArrayOutputStream steam = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, steam);
        byte[] buffer = steam.toByteArray();
        try {
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.write(buffer);
        } catch (Exception e) {
            return null;
        }
        try {
            steam.close();
            accessFile.close();
        } catch (IOException e) {
            // Note: do nothing.
        }
        return path;
    }


    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 获取bitmap尺寸缩放解决OOM问题.
     *
     * @throws IOException
     */
    public static Bitmap convertBitmap(File file) {
        Bitmap bitmap = null;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }
            BitmapFactory.Options op = new BitmapFactory.Options();
            op.inSampleSize = scale;
            fis = new FileInputStream(file.getAbsolutePath());
            bitmap = BitmapFactory.decodeStream(fis, null, op);
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap getFileForBitmap(File file) {
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            bitmap = BitmapFactory.decodeStream(fis);
            fis.close();
        } catch (IOException e) {
            return null;
        }
        return bitmap;
    }

    /**
     * 按比例缩放图片，防止大图片 内存溢出
     *
     * @param filePath
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeBitmapFromFile(String filePath, int reqWidth,
                                              int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置这个，只得到能解析的出来图片的大小，并不进行真正的解析
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 跟指定大小比较，计算缩放比例
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeBitmapFromByte(byte[] data, int reqWidth,
                                              int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;// 设置这个，只得到能解析的出来图片的大小，并不进行真正的解析
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 跟指定大小比较，计算缩放比例
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    public static Bitmap decodeBitmapFromFileScale(String filePath,
                                                   int inSampleSize) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static Bitmap decodeBitmapFromResourcesScale(Resources resources, int id, int
            inSampleSize) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeResource(resources, id, options);
    }

    public static Bitmap decodeBitmapFromByteScale(byte[] data, int inSampleSize) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        // Calculate inSampleSize
        options.inSampleSize = inSampleSize;
        // Decode bitmap with inSampleSize set
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 获取Bitmap原图防止内存溢出
     *
     * @param file
     * @return
     */
    public static Bitmap compress(File file, int kb) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inPreferredConfig = Config.ARGB_8888;

        options.inPurgeable = true;// 允许可清除

        options.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果

        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        return compressImage(bitmap, kb);
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int... maxKb) {
        ByteArrayInputStream isBm = (ByteArrayInputStream) compressStream(
                image, maxKb);
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            if (isBm != null)
                isBm.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    public static Bitmap test(String imagePath) {
        // 设置参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 只获取图片的大小信息，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 2; // 默认像素压缩比例，压缩为原图的1/2
        int minLen = Math.min(height, width); // 原图的最小边长
        if (minLen > 100) { // 如果原始图像的最小边长大于100dp（此处单位我认为是dp，而非px）
            float ratio = (float) minLen / 100.0f; // 计算像素压缩比例
            inSampleSize = (int) ratio;
        }
        options.inJustDecodeBounds = false; // 计算好压缩比例后，这次可以去加载原图了
        options.inSampleSize = inSampleSize; // 设置为刚才计算的压缩比例
        Bitmap bm = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        Log.w("TAG", "size: " + bm.getByteCount() + " width: " + bm.getWidth() + " heigth:" + bm
                .getHeight()); // 输出图像数据
        return bm;
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;

        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
            // 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10

            LogUtils.e("测试是否大于100kb");
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//
        // 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//
        // 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static InputStream compressStream(Bitmap image, int... maxKb) {
        int len = image.getByteCount();  // 原始图片的byte[]数组大小
        Log.e("图片的压缩之前", len + "");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        int count = maxKb == null || maxKb.length == 0 ? 1024 : maxKb[0];
        while (baos.toByteArray().length / 1024 > count) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            options -= 1;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//
            // 这里压缩options%，把压缩后的数据存放到baos中
        }
        image.recycle();
        try {
            if (baos != null)
                baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("图片的压缩后", baos.size() + "");
        return new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     *
     * @param srcPath
     * @return
     */
    public static InputStream getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1280;// 这里设置高度为800f
        float ww = 720;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressStream(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     *
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory
            // .decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 1920f;// 这里设置高度为800f
        float ww = 1080f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        if (baos != null) {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * View转换为Bitmap
     *
     * @param v View
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
                v.getLayoutParams().height, Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        v.setDrawingCacheEnabled(false);
        return b;
    }

    public static Bitmap getTransparentBitmap(Bitmap sourceImg, int number) {
        int[] argb = new int[sourceImg.getWidth() * sourceImg.getHeight()];

        sourceImg.getPixels(argb, 0, sourceImg.getWidth(), 0, 0, sourceImg

                .getWidth(), sourceImg.getHeight());// 获得图片的ARGB值

        number = number * 255 / 100;

        for (int i = 0; i < argb.length; i++) {

            argb[i] = (number << 24) | (argb[i] & 0x00FFFFFF);

        }

        sourceImg = Bitmap.createBitmap(argb, sourceImg.getWidth(), sourceImg

                .getHeight(), Config.ARGB_8888);

        return sourceImg;
    }

    public static class AccessImgResult {
        private boolean isAccessOk = true;
        private byte[] imgBytes;

        public boolean isAccessOk() {
            return isAccessOk;
        }

        public void setAccessOk(boolean isAccessOk) {
            this.isAccessOk = isAccessOk;
        }

        public byte[] getImgBytes() {
            return imgBytes;
        }

        public void setImgBytes(byte[] imgBytes) {
            this.imgBytes = imgBytes;
        }

    }

    /**
     * 根据网络图片路径获得图片
     *
     * @param url
     * @return
     */
    public static AccessImgResult returnBitMap(String url, String path) {
        AccessImgResult result = new AccessImgResult();
        URL myFileUrl = null;
        try {
            myFileUrl = new URL(url);

            File file = new File(path);
            if (!file.exists()) {
                File fileParent = file.getParentFile();
                if (!fileParent.exists())
                    fileParent.mkdirs();
            }
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(file);
            byte[] data = null;
            try {
                data = BaseFileUtils.readInputStream(is);
            } catch (Exception e1) {
                result.isAccessOk = false;
            }
            if (data != null)
                os.write(data);
            if (!file.exists()) {
                result.isAccessOk = false;
                result.imgBytes = data;
            }
            is.close();
            os.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            result.isAccessOk = false;
        } catch (IOException e) {
            result.isAccessOk = false;
        }
        return result;
    }

    /**
     * 根据网络图片路径获得图片
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitMap(String url, Integer inSampleSize) {
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            if (inSampleSize == null)
                inSampleSize = 2;
            try {
                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高
                opts.inSampleSize = inSampleSize;
                opts.inPreferredConfig = Config.RGB_565;
                opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true
                opts.inPurgeable = true;
                opts.inInputShareable = true;
                opts.inDither = false;
                opts.inPurgeable = true;
                opts.inTempStorage = new byte[16 * 1024];
                bitmap = BitmapFactory.decodeStream(is, null, opts);
                Log.i("Bitmap-Size--------", String.valueOf(is.available()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 根据指定的控件宽高放大bitmap,传进去的bitmap对象会自动recycle掉
     *
     * @param bitmap       需要放大的图片
     * @param widgetWidht  控件宽
     * @param widgetHeight 控件高
     */
    public static Bitmap zoomSpecifiedWidthAndHeight(Bitmap bitmap,
                                                     int widgetWidht, int widgetHeight) {
        Matrix matrix = new Matrix();
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scaleHeight = ((float) widgetHeight / height);
        float scaleWidth = ((float) widgetWidht / width);
        matrix.postScale(scaleHeight, scaleHeight);
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,
                true);
        bitmap.recycle();
        return newBitmap;
    }

    // 显示原生图片比例大小
    public static Bitmap getPathBitmap(File f, int dw, int dh) {
        Bitmap b = null;
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            if (o.outHeight > dw || o.outWidth > dh) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(dw
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (IOException e) {
        }
        return b;
    }

    public static synchronized Bitmap decodeSampledBitmapFromFile(
            String filename, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filename, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further.
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    /**
     * @param bitmap     原图
     * @param edgeLength 希望得到的正方形部分的边长
     * @return 缩放截取正中部分后的位图。
     */
    public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength) {
        if (null == bitmap || edgeLength <= 0) {
            return null;
        }

        Bitmap result = bitmap;
        int widthOrg = bitmap.getWidth();
        int heightOrg = bitmap.getHeight();

        if (widthOrg > edgeLength && heightOrg > edgeLength) {
            //压缩到一个最小长度是edgeLength的bitmap
            int longerEdge = (int) (edgeLength * Math.max(widthOrg, heightOrg) / Math.min
                    (widthOrg, heightOrg));
            int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
            int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
            Bitmap scaledBitmap;

            try {
                scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
            } catch (Exception e) {
                return null;
            }

            //从图中截取正中间的正方形部分。
            int xTopLeft = (scaledWidth - edgeLength) / 2;
            int yTopLeft = (scaledHeight - edgeLength) / 2;

            try {
                result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength,
                        edgeLength);
                scaledBitmap.recycle();
            } catch (Exception e) {
                return null;
            }
        }

        return result;
    }

}
