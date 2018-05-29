package com.donut.app.http.message;

import android.os.Parcel;
import android.os.Parcelable;

public class StarListDetail implements Parcelable
{

    private String starId;

    private String starName;

    private String headPic;

    /**
     * @return starId
     */
    public String getStarId()
    {
        return starId;
    }

    /**
     * @param starId
     *            the starId to set
     */
    public void setStarId(String starId)
    {
        this.starId = starId;
    }

    /**
     * @return starName
     */
    public String getStarName()
    {
        return starName;
    }

    /**
     * @param starName
     *            the starName to set
     */
    public void setStarName(String starName)
    {
        this.starName = starName;
    }

    /**
     * @return headPic
     */
    public String getHeadPic()
    {
        return headPic;
    }

    /**
     * @param headPic
     *            the headPic to set
     */
    public void setHeadPic(String headPic)
    {
        this.headPic = headPic;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.starId);
        dest.writeString(this.starName);
        dest.writeString(this.headPic);
    }

    public StarListDetail()
    {
    }

    protected StarListDetail(Parcel in)
    {
        this.starId = in.readString();
        this.starName = in.readString();
        this.headPic = in.readString();
    }

    public static final Creator<StarListDetail> CREATOR = new Creator<StarListDetail>()
    {
        @Override
        public StarListDetail createFromParcel(Parcel source)
        {
            return new StarListDetail(source);
        }

        @Override
        public StarListDetail[] newArray(int size)
        {
            return new StarListDetail[size];
        }
    };
}
