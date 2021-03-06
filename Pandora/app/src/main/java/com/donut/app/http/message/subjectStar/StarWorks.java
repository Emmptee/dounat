package com.donut.app.http.message.subjectStar;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/20.
 */
public class StarWorks implements Parcelable {

    private String uuid;

    private String name;

    private String publicPic;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPublicPic()
    {
        return publicPic;
    }

    public void setPublicPic(String publicPic)
    {
        this.publicPic = publicPic;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.name);
        dest.writeString(this.publicPic);
    }

    public StarWorks() {
    }

    protected StarWorks(Parcel in) {
        this.uuid = in.readString();
        this.name = in.readString();
        this.publicPic = in.readString();
    }

    public static final Parcelable.Creator<StarWorks> CREATOR = new Parcelable.Creator<StarWorks>() {
        @Override
        public StarWorks createFromParcel(Parcel source) {
            return new StarWorks(source);
        }

        @Override
        public StarWorks[] newArray(int size) {
            return new StarWorks[size];
        }
    };
}
