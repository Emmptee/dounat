package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/7/1.
 */
public class CommentPraise
{
    private String praisedUserId;

    private String praisedUserName;

    private String createTime;

    private String praisedUserUrl;

    private String userType;

    private String praisedUserType;

    private Integer isMember;

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public Integer getIsMember()
    {
        return isMember;
    }

    public void setIsMember(Integer isMember)
    {
        this.isMember = isMember;
    }

    public String getPraisedUserType()
    {
        return praisedUserType;
    }

    public void setPraisedUserType(String praisedUserType)
    {
        this.praisedUserType = praisedUserType;
    }

    public String getPraisedUserId()
    {
        return praisedUserId;
    }

    public void setPraisedUserId(String praisedUserId)
    {
        this.praisedUserId = praisedUserId;
    }

    public String getPraisedUserName()
    {
        return praisedUserName;
    }

    public void setPraisedUserName(String praisedUserName)
    {
        this.praisedUserName = praisedUserName;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getPraisedUserUrl()
    {
        return praisedUserUrl;
    }

    public void setPraisedUserUrl(String praisedUserUrl)
    {
        this.praisedUserUrl = praisedUserUrl;
    }

}
