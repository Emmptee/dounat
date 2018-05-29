package com.donut.app.model.galleypick.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.donut.app.model.galleypick.entities.MediaEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * FindAllUtils
 * Created by Qi on 2017/2/27.
 */
public class FindAllUtil extends AsyncTask<Context, Integer, LinkedHashMap<String, List<MediaEntity>>> {

    public interface OnLoadListener {
        void onPreExecute();

        void onPostExecute();
    }

    private LinkedHashMap<String, List<MediaEntity>> mediaFoldersMap;

    private OnLoadListener mListener;

    public FindAllUtil(OnLoadListener listener,
                       LinkedHashMap<String, List<MediaEntity>> mediaFoldersMap) {
        this.mListener = listener;
        this.mediaFoldersMap = mediaFoldersMap;
    }

    @Override
    protected LinkedHashMap<String, List<MediaEntity>> doInBackground(Context... contexts) {
        Context mContext = contexts[0].getApplicationContext();
        LinkedHashMap<String, List<MediaEntity>> mediaFoldersMap = new LinkedHashMap<>();
        String allKey = "图片和视频";
        String allVideoKey = "所有视频";
        mediaFoldersMap.put(allKey, new ArrayList<MediaEntity>());
        mediaFoldersMap.put(allVideoKey, new ArrayList<MediaEntity>());

        ContentResolver cr = mContext.getContentResolver();
        Uri mImaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = cr.query(mImaUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        while (cursor != null && cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
            MediaEntity entity = new MediaEntity(path, time, size);

            if (entity.getSize() / 1024 >= 10) {
                mediaFoldersMap.get(allKey).add(entity);
            }

            //将当前图片加入到相应的文件图集中
            File parentFile = new File(path).getParentFile();
            if (mediaFoldersMap.containsKey(parentFile.getName())) {
                mediaFoldersMap.get(parentFile.getName()).add(entity);
            } else {
                ArrayList<MediaEntity> images = new ArrayList<>();
                images.add(entity);
                mediaFoldersMap.put(parentFile.getName(), images);
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        // 扫描视频
        Uri media_uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Cursor cc = cr.query(media_uri, null, null, null,
                MediaStore.Video.Media.DATE_MODIFIED + " desc");

        Set<String> mVideoDirPaths = new HashSet<String>();
        while (cc != null && cc.moveToNext()) {
            String path = cc.getString(cc.getColumnIndex(MediaStore.Video.Media.DATA));
            long size = cc.getLong(cc.getColumnIndex(MediaStore.Video.Media.SIZE));
            long createTime = cc.getLong(cc.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
            long duration = cc.getLong(cc.getColumnIndex(MediaStore.Video.Media.DURATION));
            MediaEntity entity = new MediaEntity(path, createTime, size, duration);

            if (entity.getSize() / 1024 >= 100) {
                mediaFoldersMap.get(allKey).add(entity);
                mediaFoldersMap.get(allVideoKey).add(entity);
            }
        }
        if (cc != null) {
            cc.close();
        }

        List<MediaEntity> allKeyList = mediaFoldersMap.get(allKey);
        Collections.sort(allKeyList, new SortByDate());
        mediaFoldersMap.put(allKey, allKeyList);

        return mediaFoldersMap;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(LinkedHashMap<String, List<MediaEntity>> map) {
        super.onPostExecute(map);
        this.mediaFoldersMap.clear();
        this.mediaFoldersMap.putAll(map);
        mListener.onPostExecute();
    }

    private class SortByDate implements Comparator<MediaEntity> {
        @Override
        public int compare(MediaEntity e1, MediaEntity e2) {
            return (int) (e2.getCreateTime() - e1.getCreateTime());
        }
    }
}