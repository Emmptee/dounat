package com.donut.app.http.message;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/19.
 */
public class DeliveryAddress implements Parcelable
{
    private String uuid;

    private String consignee;

    private String phone;

    private String address;

    private Integer isDefault;

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getConsignee()
    {
        return consignee;
    }

    public void setConsignee(String consignee)
    {
        this.consignee = consignee;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public Integer getIsDefault()
    {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault)
    {
        this.isDefault = isDefault;
    }


    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(this.uuid);
        dest.writeString(this.consignee);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeValue(this.isDefault);
    }

    public DeliveryAddress()
    {
    }

    protected DeliveryAddress(Parcel in)
    {
        this.uuid = in.readString();
        this.consignee = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.isDefault = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<DeliveryAddress> CREATOR = new Parcelable.Creator<DeliveryAddress>()
    {
        @Override
        public DeliveryAddress createFromParcel(Parcel source)
        {
            return new DeliveryAddress(source);
        }

        @Override
        public DeliveryAddress[] newArray(int size)
        {
            return new DeliveryAddress[size];
        }
    };
}
