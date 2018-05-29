package com.donut.app.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PictureUtil
{
    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @return
     */
    public static String bitmapToString(String filePath)
    {

        Bitmap bm = getSmallBitmap(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth)
        {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


    /**
     * 根据路径获得突破并压缩返回bitmap用于显示
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath)
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try{
            filePath = java.net.URLDecoder.decode(filePath, "utf-8");
            BitmapFactory.decodeFile(filePath, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 480, 800);
            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
        }catch (Exception e){
            L.i("wjj","============"+e.toString());
        }

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path)
    {
        File file = new File(path);
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path)
    {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录
     *
     * @return
     */
    public static File getAlbumDir()
    {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                getAlbumName());
        if (!dir.exists())
        {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 获取保存 隐患检查的图片文件夹名称
     *
     * @return
     */
    public static String getAlbumName()
    {
        return "sheguantong";
    }

    /**
     * 将图片保存到本地时进行压缩,
     * 即将图片从Bitmap形式变为File形式时进行压缩,
     * 特点是:  File形式的图片确实被压缩了,
     * 但是当你重新读取压缩后的file为 Bitmap是,它占用的内存并没有改变
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp,File file){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(Bitmap bm, String fileName, String path) throws IOException
    {
        File foder = new File(path);
        if (!foder.exists())
        {
            foder.mkdirs();
        }
        File myCaptureFile = new File(path, fileName);
        if (!myCaptureFile.exists())
        {
            myCaptureFile.createNewFile();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
    }

    /**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
