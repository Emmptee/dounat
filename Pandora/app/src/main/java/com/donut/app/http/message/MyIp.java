package com.donut.app.http.message;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/7/7.
 */
public class MyIp  implements Parcelable
{
    private String ipId;

    private String thumbnailUrl;

    private String createTime;

    private String description;

    private String lastTime;

    private String name;

    private Integer status;//0:未审核；1：不通过；2通过

    private String approveDesc;

    private List<StarListDetail> starList;

    private String playUrl;

    public String getLastTime()
    {
        return lastTime;
    }

    public void setLastTime(String lastTime)
    {
        this.lastTime = lastTime;
    }

    public String getApproveDesc()
    {
        return approveDesc;
    }

    public void setApproveDesc(String approveDesc)
    {
        this.approveDesc = approveDesc;
    }

    public String getPlayUrl()
    {
        return playUrl;
    }

    public void setPlayUrl(String playUrl)
    {
        this.playUrl = playUrl;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public List<StarListDetail> getStarList()
    {
        return starList;
    }

    public void setStarList(List<StarListDetail> starList)
    {
        this.starList = starList;
    }

    public String getIpId() {
        return ipId;
    }

    public void setIpId(String ipId) {
        this.ipId = ipId;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.ipId);
        dest.writeString(this.thumbnailUrl);
        dest.writeString(this.createTime);
        dest.writeString(this.description);
        dest.writeString(this.lastTime);
        dest.writeString(this.name);
        dest.writeValue(this.status);
        dest.writeString(this.approveDesc);
        dest.writeTypedList(this.starList);
        dest.writeString(this.playUrl);
    }

    public MyIp()
    {
    }

    protected MyIp(Parcel in)
    {
        this.ipId = in.readString();
        this.thumbnailUrl = in.readString();
        this.createTime = in.readString();
        this.description = in.readString();
        this.lastTime = in.readString();
        this.name = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.approveDesc = in.readString();
        this.starList = in.createTypedArrayList(StarListDetail.CREATOR);
        this.playUrl = in.readString();
    }

    public static final Creator<MyIp> CREATOR = new Creator<MyIp>()
    {
        @Override
        public MyIp createFromParcel(Parcel source)
        {
            return new MyIp(source);
        }

        @Override
        public MyIp[] newArray(int size)
        {
            return new MyIp[size];
        }
    };
}