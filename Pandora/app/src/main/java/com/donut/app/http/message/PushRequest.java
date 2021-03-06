package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/24.
 */
public class PushRequest
{
    /** 推送用户ID */
    private String pushUserid;

    /** 推送频道ID */
    private String pushChannelid;

    /** 设备型号 */
    private String deviceType;

    /** 系统版本 */
    private String sysVersion;

    /** 分辨率 */
    private String resolution;

    /** 运营商 */
    private String operator;

    /** 渠道 */
    private String channel;

    /** 系统类型 0：安卓，1：IOS */
    private String osType;

    /** 用户类型 1：需方 0：艺人 */
    private String userType;

    /** 用户id */
    private String userId;

    /**
     * 是否接受推送 0：接受 1：不接受
     * */
    private String pushStatus;

    /**
     * 0:未登陆 1：登陆
     */
    private String status;

    private Integer a01Status;

    public Integer getA01Status()
    {
        return a01Status;
    }

    public void setA01Status(Integer a01Status)
    {
        this.a01Status = a01Status;
    }

    public String getPushUserid()
    {
        return pushUserid;
    }

    public void setPushUserid(String pushUserid)
    {
        this.pushUserid = pushUserid;
    }

    public String getPushChannelid()
    {
        return pushChannelid;
    }

    public void setPushChannelid(String pushChannelid)
    {
        this.pushChannelid = pushChannelid;
    }

    public String getDeviceType()
    {
        return deviceType;
    }

    public void setDeviceType(String deviceType)
    {
        this.deviceType = deviceType;
    }

    public String getSysVersion()
    {
        return sysVersion;
    }

    public void setSysVersion(String sysVersion)
    {
        this.sysVersion = sysVersion;
    }

    public String getResolution()
    {
        return resolution;
    }

    public void setResolution(String resolution)
    {
        this.resolution = resolution;
    }

    public String getOperator()
    {
        return operator;
    }

    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getOsType()
    {
        return osType;
    }

    public void setOsType(String osType)
    {
        this.osType = osType;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getPushStatus()
    {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus)
    {
        this.pushStatus = pushStatus;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
