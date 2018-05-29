package com.donut.app.http.message;

import java.util.List;

/**
 * Created by wujiaojiao on 2016/7/1.
 */
public class ContentComments
{
    private String contentId;

    private String commentId;

    private String commentatorId;

    private String commentatorUrl;

    private String commentatorName;

    private String content;

    private String createTime;

    private int praiseNum;

    private String address;

    private String ipAddress;

    private String userType;

    private String rewardNum;

    private String updateTime;

    private String isReplied;//0：回复过；1：未回复过

    private String isPraised;

    private int commentNum;

    private List<SubComments> subCommentsList;

    private List<CommentPraise> praiseList;

    private String subjectId;
    /**
     * 0:专题 1:专题历史 2:挑战 3:IP征集,4心愿，5街拍街拍
     */
    private String type;

    private String associationId;

    private String imgUrl;

    private String userName;

    private String title;

    private String description;

    private Integer isMember;

    /**
     * 0 心愿未达成, 1 已达成
     */
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Integer getIsMember()
    {
        return isMember;
    }

    public void setIsMember(Integer isMember)
    {
        this.isMember = isMember;
    }

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public int getCommentNum()
    {
        return commentNum;
    }

    public void setCommentNum(int commentNum)
    {
        this.commentNum = commentNum;
    }

    public String getIsReplied()
    {
        return isReplied;
    }

    public void setIsReplied(String isReplied)
    {
        this.isReplied = isReplied;
    }

    public String getIsPraised()
    {
        return isPraised;
    }

    public void setIsPraised(String isPraised)
    {
        this.isPraised = isPraised;
    }

    public String getCommentId()
    {
        return commentId;
    }

    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }

    public String getCommentatorId()
    {
        return commentatorId;
    }

    public void setCommentatorId(String commentatorId)
    {
        this.commentatorId = commentatorId;
    }

    public String getCommentatorUrl()
    {
        return commentatorUrl;
    }

    public void setCommentatorUrl(String commentatorUrl)
    {
        this.commentatorUrl = commentatorUrl;
    }

    public String getCommentatorName()
    {
        return commentatorName;
    }

    public void setCommentatorName(String commentatorName)
    {
        this.commentatorName = commentatorName;
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

    public int getPraiseNum()
    {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum)
    {
        this.praiseNum = praiseNum;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getIpAddress()
    {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getRewardNum()
    {
        return rewardNum;
    }

    public void setRewardNum(String rewardNum)
    {
        this.rewardNum = rewardNum;
    }

    public List<SubComments> getSubCommentsList()
    {
        return subCommentsList;
    }

    public void setSubCommentsList(List<SubComments> subCommentsList)
    {
        this.subCommentsList = subCommentsList;
    }

    public List<CommentPraise> getPraiseList()
    {
        return praiseList;
    }

    public void setPraiseList(List<CommentPraise> praiseList)
    {
        this.praiseList = praiseList;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getAssociationId()
    {
        return associationId;
    }

    public void setAssociationId(String associationId)
    {
        this.associationId = associationId;
    }

    public String getImgUrl()
    {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl)
    {
        this.imgUrl = imgUrl;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public String toString()
    {
        return "ContentComments [commentId=" + commentId + ", commentatorId=" + commentatorId + ", commentatorUrl=" + commentatorUrl + ", commentatorName=" + commentatorName + ", content=" + content + ", createTime=" + createTime + ", praiseNum=" + praiseNum + ", address=" + address + ", ipAddress=" + ipAddress + ", userType=" + userType + ", rewardNum=" + rewardNum + ", updateTime=" + updateTime + ", subCommentsList=" + subCommentsList + ", praiseList=" + praiseList + "]";
    }


}
