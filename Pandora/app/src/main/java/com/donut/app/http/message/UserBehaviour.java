package com.donut.app.http.message;

import java.util.Date;

/**
 * Created by Qi on 2016/7/26.
 */
public class UserBehaviour {

    private String deviceId;

    private String funCode;

    private String fkA01;

    private int memberStatus;

    private Integer loginStatus;

    private String ipAddress;

    private String city;

    private Integer netStatus;

    private String inTime;

    private String outTime;

    private String createTime;

    private String starCode;

    private String requestJson;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFunCode() {
        return funCode;
    }

    public void setFunCode(String funCode) {
        this.funCode = funCode;
    }

    public String getFkA01() {
        return fkA01;
    }

    public void setFkA01(String fkA01) {
        this.fkA01 = fkA01;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Integer getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Integer netStatus) {
        this.netStatus = netStatus;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStarCode() {
        return starCode;
    }

    public void setStarCode(String starCode) {
        this.starCode = starCode;
    }

    public String getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(String requestJson) {
        this.requestJson = requestJson;
    }
}
