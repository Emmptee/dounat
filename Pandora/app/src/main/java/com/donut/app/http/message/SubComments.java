package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/7/1.
 */
public class SubComments
{
    private String repliedUserId;

    private String repliedUserName;

    private String beRepliedUserId;

    private String beRepliedUserName;

    private String content;

    private String createTime;

    private String repliedUserType;

    private String beRepliedUserType;

    private String repliedUserImg;

    private Integer isMember;

    public Integer getIsMember()
    {
        return isMember;
    }

    public void setIsMember(Integer isMember)
    {
        this.isMember = isMember;
    }

    public String getRepliedUserImg()
    {
        return repliedUserImg;
    }

    public void setRepliedUserImg(String repliedUserImg)
    {
        this.repliedUserImg = repliedUserImg;
    }

    public String getRepliedUserType()
    {
        return repliedUserType;
    }

    public void setRepliedUserType(String repliedUserType)
    {
        this.repliedUserType = repliedUserType;
    }

    public String getBeRepliedUserType()
    {
        return beRepliedUserType;
    }

    public void setBeRepliedUserType(String beRepliedUserType)
    {
        this.beRepliedUserType = beRepliedUserType;
    }

    public String getRepliedUserId()
    {
        return repliedUserId;
    }

    public void setRepliedUserId(String repliedUserId)
    {
        this.repliedUserId = repliedUserId;
    }

    public String getRepliedUserName()
    {
        return repliedUserName;
    }

    public void setRepliedUserName(String repliedUserName)
    {
        this.repliedUserName = repliedUserName;
    }

    public String getBeRepliedUserId()
    {
        return beRepliedUserId;
    }

    public void setBeRepliedUserId(String beRepliedUserId)
    {
        this.beRepliedUserId = beRepliedUserId;
    }

    public String getBeRepliedUserName()
    {
        return beRepliedUserName;
    }

    public void setBeRepliedUserName(String beRepliedUserName)
    {
        this.beRepliedUserName = beRepliedUserName;
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

}
