package com.donut.app.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

/**
 * Created by HIQ on 2016/1/21.
 */
public class FileUtils {

    public static final String ROOT_DIR = "AppCartoon";
    public static final String DOWNLOAD_DIR = "download";
    public static final String CACHE_DIR = "cache";
    public static final String ICON_DIR = "pics";

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取下载目录
     */
    public static String getDownloadDir(Context context) {
        return getDir(DOWNLOAD_DIR, context);
    }

    /**
     * 获取缓存目录
     */
    public static String getCacheDir(Context context) {
        return getDir(CACHE_DIR, context);
    }

    /**
     * 获取icon目录
     */
    public static String getIconDir(Context context) {
        return getDir(ICON_DIR, context);
    }

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name, Context context) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getCachePath(context));
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    public static String getSDDir(String name, Context context) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            sb.append(File.separator);
        } else {
            sb.append(getCachePath(context));
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }

    /**
     * 获取SD下的应用目录
     */
    public static String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }

    /**
     * 获取应用的cache目录
     */
    public static String getCachePath(Context context) {
        File f = context.getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    /**
     * 获取应用的cache目录
     */
    public static String getExternalStorageCachePath(Context context) {
        File f = context.getExternalCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    /**
     * 获取应用的cache目录
     */
    public static String getCachePath(Context context, String dirName) {

        String cachePath = "";

        File outF = context.getExternalCacheDir();
        if (outF == null) {
            cachePath = context.getCacheDir().getAbsolutePath() + File.separator + dirName;
        } else {
            cachePath = outF.getAbsolutePath() + File.separator + dirName;
        }

        File cacheFile = new File(cachePath);
        if (!cacheFile.exists() || !cacheFile.isDirectory()) {
            cacheFile.mkdirs();
        }

        return cachePath;
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     * @return 是否写入成功
     */
    public static boolean writeFile(byte[] content, String path, boolean append) {
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        } catch (Exception e) {
//			LogUtils.e(e);
        } finally {
//			IOUtils.close(raf);
        }
        return res;
    }

    public static boolean copyFile(File srcFile, File destFile) {
        if (destFile.exists()) {
            destFile.delete();
        }

        // 复制文件
        int byteRead = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024 * 5];

            while ((byteRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }
            out.flush();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static long getDirSize(File dir) {
        if (dir == null) {
            return 0;
        }
        if (!dir.isDirectory()) {
            return 0;
        }
        long dirSize = 0;
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                dirSize += file.length();
            } else if (file.isDirectory()) {
                dirSize += file.length();
                dirSize += getDirSize(file); // 递归调用继续统计
            }
        }
        return dirSize;
    }
}
