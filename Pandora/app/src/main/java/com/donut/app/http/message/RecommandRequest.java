package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/7/28.
 */
public class RecommandRequest
{
    String contentId;
    String operation;//0：看好；1：取消看好

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }
}
