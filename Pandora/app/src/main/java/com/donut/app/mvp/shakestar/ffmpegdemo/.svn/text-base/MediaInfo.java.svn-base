package com.donut.app.mvp.shakestar.ffmpegdemo;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;

public class MediaInfo {
    String fileName;
    String width;
    String height;
    String fps;
    int duration;
    public MediaInfo(String path){
        this.fileName = path;
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(path);
        width = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        height = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        duration = Integer.valueOf(retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        retr.release();
    }

    public String getFilePath() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Bitmap getCoverBitmap(){
        MediaMetadataRetriever retr = new MediaMetadataRetriever();
        retr.setDataSource(fileName);
        return retr.getFrameAtTime(0);
    }


}
