package com.donut.app.entity;

/**
 * 用户信息
 * 
 */
public class UserInfo
{

    private long id;

    private String userId;

    private String userName;

    private String passwd;

    private String token;

    private String imgUrl;

    private String nickName;

    private int userType;

    private int thirdTag;

    private String thirdTagUid;

    private float mBalance;

    private int memberStatus;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     * @return userId
     */
    public String getUserId()
    {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /**
     * @return userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return passwd
     */
    public String getPasswd()
    {
        return passwd;
    }

    /**
     * @param passwd
     *            the passwd to set
     */
    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }

    /**
     * @return imgUrl
     */
    public String getImgUrl()
    {
        return imgUrl;
    }

    /**
     * @param imgUrl
     *            the imgUrl to set
     */
    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    /**
     * @return nickName
     */
    public String getNickName()
    {
        return nickName;
    }

    /**
     * @param nickName
     *            the nickName to set
     */
    public void setNickName(String nickName)
    {
        this.nickName = nickName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public float getmBalance() {
        return mBalance;
    }

    public void setmBalance(float mBalance) {
        this.mBalance = mBalance;
    }

    public int getThirdTag() {
        return thirdTag;
    }

    public void setThirdTag(int thirdTag) {
        this.thirdTag = thirdTag;
    }

    public String getThirdTagUid()
    {
        return thirdTagUid;
    }

    public void setThirdTagUid(String thirdTagUid)
    {
        this.thirdTagUid = thirdTagUid;
    }
}
