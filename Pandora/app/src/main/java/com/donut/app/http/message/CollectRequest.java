package com.donut.app.http.message;

/**
 * Created by wujiaojiao on 2016/5/30.
 */
public class CollectRequest
{
    String contentId;
    Integer type; //0:专题 1:专题历史（专题和专题历史，都显示在收藏的专题里）2:挑战 3:IP征集
    Integer status;//0：取消收藏  其它：添加收藏

    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getContentId()
    {
        return contentId;
    }

    public void setContentId(String contentId)
    {
        this.contentId = contentId;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }
}
