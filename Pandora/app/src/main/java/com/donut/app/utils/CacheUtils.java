package com.donut.app.utils;

import android.content.Context;

import java.io.File;

/**
 * Created by HIQ
 * on 2017/06/20.
 */

public class CacheUtils {

    public static void clear(Context context) {
        String externalCache = FileUtils.getExternalStorageCachePath(context);
        if (externalCache != null) {
            File directory = new File(externalCache);
            deleteFile(directory.listFiles());
        }

        String dataCache = FileUtils.getCachePath(context);
        if (dataCache != null) {
            File directory = new File(dataCache);
            deleteFile(directory.listFiles());
        }
    }

    private static void deleteFile(File[] files) {
        if (files == null || files.length <= 0) {
            return;
        }
        for (File file : files) {
            if (file.isDirectory()) {
                File[] fs = file.listFiles();
                if (fs.length <= 0) {
                    file.delete();
                } else {
                    deleteFile(fs);
                }
            } else {
                file.delete();
            }
        }
    }
}
