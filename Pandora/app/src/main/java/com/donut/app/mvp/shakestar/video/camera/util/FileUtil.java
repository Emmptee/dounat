package com.donut.app.mvp.shakestar.video.camera.util;

import android.graphics.Bitmap;
import android.os.Environment;

import com.socks.library.KLog;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileUtil {
    private static final File parentPath = Environment.getExternalStorageDirectory();
    private static String storagePath = "";
    private static String DST_FOLDER_NAME = "JCamera";
    private static String savePath;

    public static String choseSavePath() {
        savePath = Environment.getExternalStorageDirectory().getPath() + "/PandoraVideo";
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return savePath;
    }

    public static String saveBitmap(String dir, Bitmap b) {
        DST_FOLDER_NAME = dir;
        String path = choseSavePath();
        long dataTake = System.currentTimeMillis();
        String jpegName = path + File.separator + "picture_" + dataTake + ".jpg";
        try {
            FileOutputStream fout = new FileOutputStream(jpegName);
            BufferedOutputStream bos = new BufferedOutputStream(fout);
            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return jpegName;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static void savePicture(String dir , Bitmap bitmap){
        File file = new File(dir);
        file.mkdirs();// 创建文件夹
        String fileName = dir+"videoThumbnail.jpg";
        try {
            FileOutputStream outputStream = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);// 把数据写入文件
            outputStream.flush();
            outputStream.close();
            KLog.e("保存文件名为---" + fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static boolean deleteFile(String url) {
        boolean result = false;
        File file = new File(url);
        if (file.exists()) {
            result = file.delete();
        }
        return result;
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}
