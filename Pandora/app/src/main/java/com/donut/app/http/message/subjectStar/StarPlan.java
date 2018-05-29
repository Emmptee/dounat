package com.donut.app.http.message.subjectStar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/20.
 */
public class StarPlan implements Parcelable {

    private String uuid;

    private String content;

    private String createTime;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.content);
        dest.writeString(this.createTime);
    }

    public StarPlan() {
    }

    protected StarPlan(Parcel in) {
        this.uuid = in.readString();
        this.content = in.readString();
        this.createTime = in.readString();
    }

    public static final Parcelable.Creator<StarPlan> CREATOR = new Parcelable.Creator<StarPlan>() {
        @Override
        public StarPlan createFromParcel(Parcel source) {
            return new StarPlan(source);
        }

        @Override
        public StarPlan[] newArray(int size) {
            return new StarPlan[size];
        }
    };
}
