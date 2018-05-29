package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/30.
 */
public class PraiseRequest
{
    String contentId;

    Integer praiseType;//是否点赞 1：点赞 2：取消

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public Integer getPraiseType()
    {
        return praiseType;
    }

    public void setPraiseType(Integer praiseType)
    {
        this.praiseType = praiseType;
    }
}
