package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/9/7.
 */
public class DeviceRequest
{
    String deviceId;

    String phoneType;

    public String getDeviceId()
    {

        return deviceId;
    }

    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }

    public String getPhoneType()
    {
        return phoneType;
    }

    public void setPhoneType(String phoneType)
    {
        this.phoneType = phoneType;
    }
}