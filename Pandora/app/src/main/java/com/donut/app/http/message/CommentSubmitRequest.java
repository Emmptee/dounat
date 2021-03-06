package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/30.
 */
public class CommentSubmitRequest
{
    private String contentId;//对专题内容评论时传入

    private String commentId;//对评论回复时传入

    private String content;

    private String operationType;//0：对专题内容评论；1：对评论回复

    private String beRepliedId;//只有对评论的回复去回复时需要传入

    private String rewardNum;//打赏并评论时传入

    private String userContent;

    public String getUserContent()
    {
        return userContent;
    }

    public void setUserContent(String userContent)
    {
        this.userContent = userContent;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCommentId()
    {
        return commentId;
    }

    public void setCommentId(String commentId)
    {
        this.commentId = commentId;
    }

    public String getOperationType()
    {
        return operationType;
    }

    public void setOperationType(String operationType)
    {
        this.operationType = operationType;
    }

    public String getBeRepliedId()
    {
        return beRepliedId;
    }

    public void setBeRepliedId(String beRepliedId)
    {
        this.beRepliedId = beRepliedId;
    }

    public String getRewardNum()
    {
        return rewardNum;
    }

    public void setRewardNum(String rewardNum)
    {
        this.rewardNum = rewardNum;
    }
}
