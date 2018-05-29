package com.donut.app.model.galleypick.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * MediaEntity
 * Created by Qi on 2017/2/27.
 */

public class MediaEntity implements Parcelable {
    //文件路径
    private String path;
    //文件被加入到Media中的时间
    private long createTime;
    //文件的大小
    private long size;

    private boolean isVideo;

    //视频时长(毫秒)
    private long duration;

    public MediaEntity(String path, long time, long size) {
        this.path = path;
        this.createTime = time;
        this.size = size;
        this.isVideo = false;
    }

    public MediaEntity(String path, long time, long size, long duration) {
        this.path = path;
        this.createTime = time;
        this.size = size;
        this.duration = duration;
        this.isVideo = true;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeLong(this.createTime);
        dest.writeLong(this.size);
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeLong(this.duration);
    }

    protected MediaEntity(Parcel in) {
        this.path = in.readString();
        this.createTime = in.readLong();
        this.size = in.readLong();
        this.isVideo = in.readByte() != 0;
        this.duration = in.readLong();
    }

    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel source) {
            return new MediaEntity(source);
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };
}
