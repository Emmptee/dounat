package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/7/6.
 */
public class DeleteRequest
{
    String IPId;

    String contentId;

    public String getIPId()
    {
        return IPId;
    }

    public void setIPId(String IPId)
    {
        this.IPId = IPId;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }
}
