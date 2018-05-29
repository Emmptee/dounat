package com.donut.app.http.message;

import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

public class SubjectListDetail extends BaseObservable implements Parcelable {

    private String subjectId;

    private String b02Id;

    private String channelId;

    private String channelName;

    private int channelType;

    private String name;

    private String description;

    private Integer period;

    private String publicPic;

    private String starId;

    private String starName;

    private Integer status;

    private Integer collectionStatus;

    private String createTime;

    private String headPic;

    private long browseTimes;

    private boolean emptyData;

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getB02Id() {
        return b02Id;
    }

    public void setB02Id(String b02Id) {
        this.b02Id = b02Id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getPublicPic() {
        return publicPic;
    }

    public void setPublicPic(String publicPic) {
        this.publicPic = publicPic;
    }

    public String getStarId() {
        return starId;
    }

    public void setStarId(String starId) {
        this.starId = starId;
    }

    public String getStarName() {
        return starName;
    }

    public void setStarName(String starName) {
        this.starName = starName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(Integer collectionStatus) {
        this.collectionStatus = collectionStatus;
        notifyChange();
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    public long getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(long browseTimes) {
        this.browseTimes = browseTimes;
        notifyChange();
    }

    public boolean isEmptyData() {
        return emptyData;
    }

    public void setEmptyData(boolean emptyData) {
        this.emptyData = emptyData;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subjectId);
        dest.writeString(this.b02Id);
        dest.writeString(this.channelId);
        dest.writeString(this.channelName);
        dest.writeValue(this.channelType);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeValue(this.period);
        dest.writeString(this.publicPic);
        dest.writeString(this.starId);
        dest.writeString(this.starName);
        dest.writeValue(this.status);
        dest.writeValue(this.collectionStatus);
        dest.writeString(this.createTime);
        dest.writeString(this.headPic);
        dest.writeLong(this.browseTimes);
        dest.writeByte(this.emptyData ? (byte) 1 : (byte) 0);
    }

    public SubjectListDetail() {
    }

    protected SubjectListDetail(Parcel in) {
        this.subjectId = in.readString();
        this.b02Id = in.readString();
        this.channelId = in.readString();
        this.channelName = in.readString();
        this.channelType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.name = in.readString();
        this.description = in.readString();
        this.period = (Integer) in.readValue(Integer.class.getClassLoader());
        this.publicPic = in.readString();
        this.starId = in.readString();
        this.starName = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.collectionStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createTime = in.readString();
        this.headPic = in.readString();
        this.browseTimes = in.readLong();
        this.emptyData = in.readByte() != 0;
    }

    public static final Creator<SubjectListDetail> CREATOR = new Creator<SubjectListDetail>() {
        @Override
        public SubjectListDetail createFromParcel(Parcel source) {
            return new SubjectListDetail(source);
        }

        @Override
        public SubjectListDetail[] newArray(int size) {
            return new SubjectListDetail[size];
        }
    };
}
