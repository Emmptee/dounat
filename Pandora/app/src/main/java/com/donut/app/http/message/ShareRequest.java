package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/30.
 */
public class ShareRequest
{
    String contentId;
    String subjectId;

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getSubjectId()
    {
        return subjectId;
    }

    public void setSubjectId(String subjectId)
    {
        this.subjectId = subjectId;
    }
}
