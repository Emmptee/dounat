package com.donut.app.http.message;

public class UserLoginResponse extends BaseResponse
{
    private String userId;

    private String token;

    private String nickName;
    
    private String imgUrl;

    private int isBandingPhone;

    private int userType;

    private float balance;

    private int isFirstLogin;

    int isFirst;//0：否 1：是

    private String picPath;

    private int memberStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsBandingPhone() {
        return isBandingPhone;
    }

    public void setIsBandingPhone(int isBandingPhone) {
        this.isBandingPhone = isBandingPhone;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(int isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public int getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(int isFirst) {
        this.isFirst = isFirst;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }
}
